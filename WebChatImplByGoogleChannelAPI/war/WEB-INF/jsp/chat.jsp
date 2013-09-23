<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="chat.ChatService"%>
<%@ page import="java.util.Set"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" type="text/css" href="css/960.css" />
<script type="text/javascript" src="js/jquery-1.6.min.js"></script>
<script src='/_ah/channel/jsapi'></script>
<title>Channel API Chat Demo</title>
<script type="text/javascript">
    <%
    String contextPath = this.getServletContext().getContextPath();
    if (contextPath == null) {
        contextPath = "";
    }
    String currentUserName = request.getParameter(ChatService.USER_NAME_PARAM);
    %>
    contextPath = "<%=contextPath%>";
    chatUserName = "<%=currentUserName%>";
    chatMessageParam = "<%=ChatService.CHAT_MESSAGE_PARAM%>";
    chatTargetParam ="<%=ChatService.TARGET_PARAM%>";
    chatUserNameParam = "<%=ChatService.USER_NAME_PARAM%>";
    channelToken = "<%=(String)request.getAttribute(ChatService.CHANNELTOKEN_ATTR_NAME)%>";
</script>
<script type="text/javascript" src="js/activity.js"></script>
<script type="text/javascript" src="js/channel.js"></script>
</head>
<body onunload="onClosed()">

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
					            if (!user.equalsIgnoreCase(currentUserName)) {
					%>
					<option id="u2<%=user%>" value="<%=user%>"><%=user%></option>
					<%
					    }
					        }
					    }
					%>
				</select> 
				 <input type="text" id="message" size="40"></input>
				 <input type="button" value="發言" onclick="sendMessage()">
			</div>
		</div>
		<div class="grid_2">
			<h3>使用者列表</h3>
			<ol id="userList">
				<%
				    if (userSet != null) {
				        for (String user : userSet) {
				            if (!user.equalsIgnoreCase(currentUserName)) {
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
