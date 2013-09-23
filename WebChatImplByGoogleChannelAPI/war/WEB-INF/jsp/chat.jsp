<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="chat.ChatService"%>
<%@ page import="java.util.Set"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script type="text/javascript" src="js/jquery-1.6.min.js"></script>
<link rel="stylesheet" type="text/css" href="css/960.css" />
<script src='/_ah/channel/jsapi'></script>
<title>Channel API Chat Demo</title>
<%
    String channelToken = (String) request
            .getAttribute(ChatService.CHANNELTOKEN_ATTR_NAME);
    String contextPath = this.getServletContext().getContextPath();
    if (contextPath == null) {
        contextPath = "";
    }
%>
<script type="text/javascript" src="js/channel.js">
var socket;
//add new user

function onOpened() {
};

function openChannel() {
	var channel = new goog.appengine.Channel("<%=channelToken%>");
	var handler = {
		'onopen' : onOpened,
		'onmessage' : onMessage,
		'onerror' : onError,
		'onclose' : onClosed,
	};
	socket = channel.open(handler);
};

function onMessage(m){
	response = JSON.parse(m.data);
	if (response.message != null && response.sender != null
			&& response.broadCast != null) {
		if (response.broadCast == 'true' ) {
		    var msg = response.sender + " said to All : "
				+ response.message;
		    newMessage(msg);
	    } else {
		    var msg = response.sender + " said to you : "
			+ response.message;
		    privateMessage(msg);
	    }
	}
	if (response.addUser != null) {
		newUser(response.addUser);
		var msg = "Welcome " + response.addUser + " join our room!";
		newMessage(msg);
	}
	if (response.removeUser != null) {
		deleteUser(response.removeUser);
		var msg = response.removeUser + " left this room!";
		newMessage(msg);
	}
};

function onError(e){
	alert("Exception has occured \n" + "Description : " + e.description
			+ "\n" + "Exception Code : " + e.code);
};

function onClosed() {
	socket.close();
};

setTimeout(openChannel, 100);
</script>
<script>
//new private message
function privateMessage(data) {
	var list = document.getElementById('privateMessage');
	var li = document.createElement('li');
	li.innerHTML = data;

	list.appendChild(li);
};

function newUser(data) {
	var list = document.getElementById('userList');
	var li = document.createElement('li');
	li.setAttribute("id", "u1" + data);
	li.innerHTML = data;
	list.appendChild(li);

	var user = document.getElementById('user');
	var option = document.createElement('option');
	option.setAttribute("id", "u2" + data);
	option.innerHTML = data;
	user.appendChild(option);
};

//remove user
function deleteUser(data) {
	$('#u1' + data).remove();
	$('#u2' + data).remove();
};

//new public message  
function newMessage(data) {
	var list = document.getElementById('messageList');
	var li = document.createElement('li');
	li.innerHTML = data;

	list.appendChild(li);
};
</script>
</head>
<body onunload="onClosed()">
	<%
	    String userName = request.getParameter(ChatService.USER_NAME_PARAM);
	%>
	<script type="text/javascript">
	function sendMessage() {
	$.ajax(
		{
		type : "POST",
		url : "<%=contextPath%>/MessageReceivingServlet",
		data: "<%=ChatService.CHAT_MESSAGE_PARAM%>=" + $("#message").val() 
		    + "&<%=ChatService.TARGET_PARAM%>=" + $("#user").val() 
		    + "&<%=ChatService.USER_NAME_PARAM%>=<%=userName%>"
			});
			var list = document.getElementById('privateMessage');
			var li = document.createElement('li');
			li.innerHTML = "I send message to " + $("#user").val() + ": "
					+ $("#message").val();
			list.appendChild(li);
		}
	</script>
	<div class="container_12">
		<div class="grid_10">
			<div>公共聊天訊息</div>
			<div id="messageList"
				style="height: 250px; overflow: scroll; border: solid 1px black;">

			</div>
			<br />
			<div>個人聊天訊息</div>
			<div id="privateMessage"
				style="height: 150px; overflow: scroll; border: solid 1px black;">

			</div>
			<br />
			<%
			    ChatService chatService = (ChatService) (getServletContext()
			            .getAttribute(ChatService.CHAT_SERVICE_NAME));
			    Set<String> userSet = chatService.getUserSet();
			%>
			<div>
				<select id="user" style="width: 100px; overflow: scroll;">
					<option value="<%=ChatService.BROADCAST_PARAM%>">全部</option>
					<%
					    if (userSet != null) {
					        for (String user : userSet) {
					            if (!user.equalsIgnoreCase(userName)) {
					%>
					<option id="u2<%=user%>" value="<%=user%>"><%=user%></option>
					<%
					    }
					        }
					    }
					%>
				</select> <input type="text" id="message" size="40"></input> <input
					type="button" value="發言" onclick="sendMessage()">
			</div>
		</div>
		<div class="grid_2">
			<h3>使用者列表</h3>
			<ol id="userList">
				<%
				    if (userSet != null) {
				        for (String user : userSet) {
				            if (!user.equalsIgnoreCase(userName)) {
				%>
				<li id="u1<%=user%>"><%=user%></li>
				<%
				    }
				        }
				    }
				%>
			</ol>
		</div>
	</div>
</body>
</html>
