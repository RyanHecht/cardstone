let socket;
let oppUser;
let userReady = false;
let oppReady = false;

$(document).ready(() => {
	socket = new LobbySocket(parseInt($.cookie("id")), isHost, onOpponentJoin, onOpponentLeave, onOpponentSetDeck, onGameStart, onLobbyCancel, handleChat);
});

function onOpponentJoin(opponentId) {
	console.log("Have opp id" + opponentId);
	const postParams = {id: opponentId};
	oppId = opponentId;
	$.post("/username", postParams, responseJSON => {
		const respObj = JSON.parse(responseJSON);
		console.log(respObj.username);
		oppUser = respObj.username;

		$("#oppmessage").text(oppUser + " has joined the game.");
		$("#oppname").text(oppUser);
		setTimeout(function() {
			$("#oppmessage").text(oppUser + " is choosing a deck")
		}, 1500);
	});

	console.log(oppUser);
};

function onOpponentLeave() {
	console.log("Opponent left");
	$("#oppname").text("Opponent");
	$("#oppmessage").text(oppUser + " left the lobby");

	setTimeout(function() {
		$("#oppmessage").text("Waiting for another player to join...");
	}, 1500);

	oppReady = false;
	console.log("Disabling button");
	$("#play").addClass("disabled");
};

function onOpponentSetDeck() {
	console.log("Opponent set deck");
	$("#oppmessage").text($("#oppname").text() + " is ready");
	oppReady = true;
	if (userReady && oppReady) {
		console.log("Enabling button");
		$("#play").removeClass("disabled");
	}
};

function onGameStart() {
	window.location.replace("/game");
	// in game route, determine whether player 
	// is supposed to be in game or nah
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

function handleChat(msg) {
	console.log("Got msg: " + msg);
};

$("#leave").on("click", function() {
	window.location.replace("/lobbies");
});

$("#play").on("click", function() {
	socket.startGame();
});

$("#deckselect").on("change", function() {
	console.log("Changed to " + $(this).val());
	const deckName = $(this).val();

	if (deckName == "Pick a deck") {
		$("#message").text("Choose a valid deck from the options above.");
	} else {
	const postParams = {deck: deckName};
		$.post("/deck_from", postParams, responseJSON => {
			const respObj = JSON.parse(responseJSON);
			console.log(respObj);
			if (respObj.cards) {
				console.log("set deck");
				console.log(respObj);
				console.log("I dun set a deckkkkkkkkkkkkkkk");
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
