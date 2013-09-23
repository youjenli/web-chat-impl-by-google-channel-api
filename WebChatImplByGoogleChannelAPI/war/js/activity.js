function privateMessage(data) {
	var list = document.getElementById('privateMessage');
	var li = document.createElement('li');
	li.innerHTML = data;

	list.appendChild(li);
};

function newUser(data) {
	var list = $('#userList')[0];
	var li = document.createElement('li');
	li.setAttribute("id", "u1" + data);
	li.innerHTML = data;
	list.appendChild(li);

	var user = $('user');
	var option = document.createElement('option');
	option.setAttribute("id", "u2" + data);
	option.innerHTML = data;
	user.appendChild(option);
};

// remove user
function deleteUser(data) {
	$('#u1' + data).remove();
	$('#u2' + data).remove();
};

// new public message
function newMessage(data) {
	var list = document.getElementById('messageList');
	var li = document.createElement('li');
	li.innerHTML = data;

	list.appendChild(li);
};

function sendMessage() {
	$.ajax({
		type : "POST",
		url : contextPath + "/MessageReceivingServlet",
		data : chatMessageParam + "=" + $("#message").val() + "&"
				+ chatTargetParam + "=" + $("#user").val() + "&"
				+ chatUserNameParam + "=" + chatUserName
	});
	var list = $("#privateMessage")[0];
	var li = document.createElement('li');
	li.innerHTML = "I send message to " + $("#user").val() + ": "
			+ $("#message").val();
	list.appendChild(li);
}