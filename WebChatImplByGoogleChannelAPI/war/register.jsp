<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="chat.ChatService"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Chat Login</title>
</head>
<body>
	<h1>Chat Log in</h1>
	<%
	   String contextPath = this.getServletContext().getContextPath();
	   if ( contextPath == null ) {
		   contextPath = "";
	   }
	%>
	<form method="post" action="<%=contextPath%>/MessageReceivingServlet">
		Input your nick name: <input 	name="<%=ChatService.USER_NAME_PARAM%>" type="text" /> 
		<input name="<%=ChatService.USER_REGISTRATION_PARAM%>" type="hidden"	 value="true" />
		<input name="submitBtn" type="submit" value="Submit"><br />
		<%
			String errorMsg = request
					.getParameter(ChatService.USER_SERVICE_EXCEPTION_ATTR);
			if (errorMsg != null) {
				out.print(errorMsg);
			}
		%>
	</form>
</body>
</html>