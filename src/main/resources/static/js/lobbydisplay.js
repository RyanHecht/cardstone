let socket;
let oppUser;
let userReady = false;
let oppReady = false;

$(document).ready(() => {
	socket = new LobbySocket(parseInt($.cookie("id")), isHost, onOpponentJoin, onOpponentLeave, onOpponentSetDeck, onGameStart, onLobbyCancel, handleChat);

	setInterval(function() {
		socket.sendHeartbeat();
	}, 5000);
});

function onOpponentJoin(opponentId) {
	const postParams = {id: opponentId};
	oppId = opponentId;
	$.post("/username", postParams, responseJSON => {
		const respObj = JSON.parse(responseJSON);
		oppUser = respObj.username;

		$("#oppmessage").text(oppUser + " has joined the game.");
		$("#oppname").text(oppUser);
		setTimeout(function() {
			$("#oppmessage").text(oppUser + " is choosing a deck")
		}, 1500);
	});
};

function onOpponentLeave() {
	$("#oppname").text("Opponent");
	$("#oppmessage").text(oppUser + " left the lobby");

	setTimeout(function() {
		$("#oppmessage").text("Waiting for another player to join...");
	}, 1500);

	oppReady = false;
	$("#play").addClass("disabled");
};

function onOpponentSetDeck() {
	$("#oppmessage").text($("#oppname").text() + " is ready");
	oppReady = true;
	if (userReady && oppReady) {
		$("#play").removeClass("disabled");
	}
};

function onGameStart() {
	window.location.replace("/game");
};

function onLobbyCancel() {
	const form = $("<form action='/lobbies' method='POST'>" +
				   " <input type='text' name='errorMsg' value='Please find another lobby'/> <input type='text' name='errorHead' value='Lobby canceled'/>");
	$('body').append(form);
	form.submit();
};

function handleChat(msg) {
};

$("#leave").on("click", function() {
	window.location.replace("/lobbies");
});

$("#play").on("click", function() {
	socket.startGame();
});

$("#deckselect").on("change", function() {
	const deckName = $(this).val();

	if (deckName == "Pick a deck") {
		$("#message").text("Choose a valid deck from the options above.");
	} else {
	const postParams = {deck: deckName};
		$.post("/deck_from", postParams, responseJSON => {
			const respObj = JSON.parse(responseJSON);
			if (respObj.cards) {
				socket.setDeck(respObj);
				$("#message").text("Deck set to " + deckName);
			}
			userReady = true;
			if (userReady && oppReady) {
				$("#play").removeClass("disabled");
			}
		});
	}
});
