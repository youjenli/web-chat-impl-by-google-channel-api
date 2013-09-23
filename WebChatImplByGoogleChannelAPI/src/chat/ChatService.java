package chat;

import java.util.Set;

public interface ChatService {

	public static final String USER_NAME_PARAM = "UserName";
	
	public String USER_SERVICE_EXCEPTION_ATTR = 
		"User service Exception :";
	
	public static final String CHAT_SERVICE_NAME = "ChatService";
	
	public String MISSING_USER_NAME = "Missing user name.";
	
	public String USER_REGISTRATION_PARAM = "registration";
	
	public String USER_NAME_IN_USE = "user name already exists";
	
	public static final String TARGET_PARAM = "target";
		
	public static final String BROADCAST_PARAM = "all";
	
	public String CHANNELTOKEN_ATTR_NAME = "channelToken";
	
	public final String CHAT_MESSAGE_PARAM = "message";

	public String addNewClient(String userName);
		
	public boolean updateUserList(String userName, boolean result);
	
	public boolean broadCastMsg(String sender, String msg);
	
	public boolean sendPrivateMsg(String sender, String reciever, String msg);
		
	public boolean removeClient(String userName);
	
	public Set<String> getUserSet();
	
}
