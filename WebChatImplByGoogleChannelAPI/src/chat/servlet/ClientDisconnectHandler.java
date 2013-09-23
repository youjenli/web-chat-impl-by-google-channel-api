package chat.servlet;

import java.io.IOException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import chat.ChatService;

import com.google.appengine.api.channel.*;
/**
 * Servlet implementation class ClientDisconnectHandler
 */
public class ClientDisconnectHandler extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	private ChatService chatService = null;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ClientDisconnectHandler() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Servlet#init(ServletConfig)
	 */
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		if (config.getServletContext().getAttribute(
				ChatService.CHAT_SERVICE_NAME) != null) {
			chatService = (ChatService) (config.getServletContext()
					.getAttribute(ChatService.CHAT_SERVICE_NAME));
		} else {
			throw new ServletException(
					"User service unavailable, unable to start message receiving service");
		}
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ChannelService channelService = ChannelServiceFactory.getChannelService();
		ChannelPresence presence = channelService.parsePresence(request);
		String clientID = presence.clientId();
		chatService.removeClient(clientID);
	}

}
