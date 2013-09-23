package chat;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.json.JSONObject;

import com.google.appengine.api.channel.ChannelMessage;
import com.google.appengine.api.channel.ChannelService;
import com.google.appengine.api.channel.ChannelServiceFactory;

public class ChatServiceImpl implements ChatService, ServletContextListener {

	private final String TEMP_DATA = "temp";
	private ConcurrentHashMap<String, String> clientMap;

	// userName channelID
	public ChatServiceImpl() {
		clientMap = new ConcurrentHashMap<String, String>();

	}

	@Override
	public boolean updateUserList(String user, boolean result) {
		String response = this.getUpdateUserListJSONObject(user, result);
		ChannelService service = ChannelServiceFactory.getChannelService();
		ChannelMessage serviceMsg = null;
		for (Entry<String, String> entry : clientMap.entrySet()) {
			if (!entry.getValue().equals(TEMP_DATA)) {
				serviceMsg = new ChannelMessage(entry.getKey(), response);
				service.sendMessage(serviceMsg);
			}
		}
		return true;
	}

	private String getUpdateUserListJSONObject(String user, boolean result) {
		Map<String, String> userList = new HashMap<String, String>();
		if (result == true) {
			userList.put("addUser", user);
		} else {
			userList.put("removeUser", user);
		}
		JSONObject json = new JSONObject(userList);
		return json.toString();
	}

	@Override
	public boolean broadCastMsg(String sender, String msg) {
		ChannelService service = ChannelServiceFactory.getChannelService();
		String json = this.getChatMessageJSONObject(sender, msg, true);
		ChannelMessage serviceMsg = null;
		for (Entry<String, String> entry : clientMap.entrySet()) {
				serviceMsg = new ChannelMessage(entry.getKey(), json);
				service.sendMessage(serviceMsg);
		}
		return true;
	}

	private String getChatMessageJSONObject(String sender, String msg,
			boolean broadCast) {
		Map<String, String> message = new HashMap<String, String>();
		message.put("message", msg);
		message.put("sender", sender);
		if (broadCast == true) {
			message.put("broadCast", "true");
		}else{
			message.put("broadCast", "false");
		}
		JSONObject json = new JSONObject(message);
		return json.toString();
	}

	@Override
	public boolean sendPrivateMsg(String sender, String receiver, String msg) {
		if ( receiver != null) {
			ChannelService service = ChannelServiceFactory.getChannelService();
			ChannelMessage message = new ChannelMessage(receiver,
					this.getChatMessageJSONObject(sender, msg, false) );
			service.sendMessage(message);
			return true;
		}
		return false;
	}

	@Override
	public String addNewClient(String userName) {
		if (userName != null) {
			if (clientMap.putIfAbsent(userName, TEMP_DATA) == null) {
				ChannelService service = ChannelServiceFactory
						.getChannelService();
				String token = service.createChannel(userName + System.currentTimeMillis() );
				if ( token != null ){
					this.updateUserList(userName, true);
					clientMap.put(userName, token);
					return token;
				}				
			}
		}
		return null;
	}

	@Override
	public boolean removeClient(String userName) {
		String result = clientMap.remove(userName);
		if (result != null) {
			this.updateUserList(userName, false);
			return true;
		}
		return false;
	}

	@Override
	public Set<String> getUserSet() {
		return clientMap.keySet();
	}

	@Override
	public void contextDestroyed(ServletContextEvent arg0) { 
		arg0.getServletContext().removeAttribute(CHAT_SERVICE_NAME);
	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		arg0.getServletContext().setAttribute(CHAT_SERVICE_NAME, this);
	}

}
