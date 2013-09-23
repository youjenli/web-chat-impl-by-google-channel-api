function privateMessage(data) { //update private message board ui
	var li = document.createElement('li');
	li.innerHTML = data;

	$('#privateMessage')[0].appendChild(li);
};

function newUser(data) { // update user list ui
	var li = document.createElement('li');
	li.setAttribute("id", "u1" + data);
	li.innerHTML = data;
	$('#userList')[0].appendChild(li);

	var option = document.createElement('option');
	option.setAttribute("id", "u2" + data);
	option.innerHTML = data;
	$('#user')[0].appendChild(option);
};

// remove user
function deleteUser(data) {// update user list ui 
	$('#u1' + data).remove();
	$('#u2' + data).remove();
};

function newMessage(data) {//update public message board ui
	var li = document.createElement('li');
	li.innerHTML = data;
	$('#messageList')[0].appendChild(li);
};

function sendMessage() {
	$.ajax({
		type : "POST",
		url : contextPath + "/MessageReceivingServlet",
		data : chatMessageParam + "=" + $("#message").val() + "&"
				+ chatTargetParam + "=" + $("#user").val() + "&"
				+ chatUserNameParam + "=" + chatUserName
	});

	var li = document.createElement('li');
	li.innerHTML = "I send message to " + $("#user").val() + ": "
			+ $("#message").val();
	$("#privateMessage")[0].appendChild(li);
}