package chat.servlet;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import chat.ChatService;

/**
 * Servlet implementation class MessageReceivingServlet
 */
public class MessageReceivingServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private ChatService chatService = null;

	public static String channelToken = "channelToken";
	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public MessageReceivingServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see Servlet#init(ServletConfig)
	 */
	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.GenericServlet#init(javax.servlet.ServletConfig)
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
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String userName = request.getParameter(ChatService.USER_NAME_PARAM);
		if (userName == null) { //Empty user name input
			request.setAttribute(ChatService.USER_SERVICE_EXCEPTION_ATTR,
					ChatService.MISSING_USER_NAME);
			RequestDispatcher dispatcher = request
					.getRequestDispatcher("/Registration");
			dispatcher.forward(request, response);
		}
		String registration = request
				.getParameter(ChatService.USER_REGISTRATION_PARAM);
		if (registration != null
		        && registration.equalsIgnoreCase("true") == true ) {
				//&& new Boolean(registration).booleanValue() == true) {
			// registration
			String token = chatService.addNewClient(userName); 
			if ( token != null) {// add new client succeed
				request.setAttribute(ChatService.CHANNELTOKEN_ATTR_NAME, token);
				request.setAttribute(ChatService.USER_NAME_PARAM, userName);
				RequestDispatcher dispatcher = request
						.getRequestDispatcher("/Chat");
				dispatcher.forward(request, response);
			} else {
				request.setAttribute(ChatService.USER_SERVICE_EXCEPTION_ATTR,
						ChatService.USER_NAME_IN_USE);
				RequestDispatcher dispatcher = request
						.getRequestDispatcher("/Registration");
				dispatcher.forward(request, response);
			}
		} else {// already registered ???
				String chatMessage = request
						.getParameter(ChatService.CHAT_MESSAGE_PARAM);
				if (chatMessage == null) {
					response.setStatus(400);
					return;
				}
				String target = request
							.getParameter(ChatService.TARGET_PARAM);
					if ( target == null || target.equalsIgnoreCase(ChatService.BROADCAST_PARAM) ){
						chatService.broadCastMsg(userName, chatMessage);
			        }else{
			        	chatService.sendPrivateMsg(userName, target,
								chatMessage);
			        }
		}
		
	}
}