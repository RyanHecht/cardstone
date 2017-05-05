let socket;
let toSpectate;

$(document).ready(() => {
	console.log(onOpponentJoin)
	socket = new SpectateLobbySocket(parseInt($.cookie("id")), onOpponentJoin, onOpponentLeave, onGameStart, onLobbyCancel);
	toSpectate = parseInt($("#hostRadio").val());
});

function onOpponentJoin(oppId) {
	console.log("Opponent joined");
	if (toSpectate == -1) {
		toSpectate = oppId;
		console.log(toSpectate);
		socket.updateSpectatee(toSpectate);
	}
	$("#otherRadio").val(oppId);
	const postParams = {id: oppId};
	$.post("/username", postParams, responseJSON => {
		const respObj = JSON.parse(responseJSON);
		const u = respObj.username;
		console.log(u);

		$("#oppname").text(u);
		$("#message").text(u + " has joined the game.");
		setTimeout(function() {
			$("#message").text("");
		}, 1000);
	});
};

function onOpponentLeave() {
	console.log("Opponent left");
	const leaver = $("#oppname").text();
	$("#oppname").text("Opponent");
	$("#message").text(leaver + " left the game");
	setTimeout(function() {
		$("#message").text("Waiting for another player to join...");
	}, 1000);

	// if they were spectating user and they leave,
	// update radio button's value and who they're
	// spectating
	if (toSpectate != parseInt($("#hostRadio").val())) {
		toSpectate = -1;
	}
	$("#otherRadio").val(-1);
};

function onLobbyCancel() {
	console.log("Lobby cancel");
	// redirect to lobbies page with
	// modal saying their lobby was canceled
	const form = $("<form action='/lobbies' method='POST'>" +
				   " <input type='text' name='errorMsg' value='Please find another lobby'/> <input type='text' name='errorHead' value='Lobby canceled'/>");
	$('body').append(form);
	form.submit();
};


function onGameStart() {
	console.log("Game starting");
	window.location.replace("/game");
};

$('input[type=radio][name=spectateRadio]').change(function() {
	toSpectate = parseInt($(this).val());
	console.log(toSpectate);
	if (toSpectate != -1) {
		socket.updateSpectatee(toSpectate);
	}
});
