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
			+ "\n" + "Exception Code : " + e.code  + "\n");
};

function onClosed() {
	socket.close();
};

setTimeout(openChannel, 500);