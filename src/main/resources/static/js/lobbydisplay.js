let socket;

$(document).ready(() => {	
	socket = new LobbySocket(parseInt($.cookie("id")), isHost, onOpponentJoin, onOpponentLeave, onOpponentSetDeck, onGameStart, onLobbyCancel, handleChat);
});

function onOpponentJoin(opponentId) {
	console.log("Have opp id" + opponentId);
	const postParams = {id: opponentId};
	let oppUsername;
	$.post("/username", postParams, responseJSON => {
		const respObj = JSON.parse(responseJSON);
		console.log(respObj.username);
		oppUsername = respObj.username;
	});
	
	$("#oppmessage").text(oppUsername + " has joined the game.");
	$("#oppname").text(oppUsername);
	
	setTimeout(function() {
		$("#oppmessage").text(oppUsername + " is choosing a deck")
	}, 1500);
};

function onOpponentLeave() {
	console.log("Opponent left");
};

function onOpponentSetDeck(deckName) {
	console.log("Opponent set deck " + deckName);
};

function onGameStart() {
	console.log("Starting game");
};

function onLobbyCancel() {
	console.log("Lobby cancel");
};

function handleChat(msg) {
	console.log("Got msg: " + msg);
}; 

$("#leave").on("click", function() {
	window.location.replace("/lobbies");
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
				socket.setDeck(respObj);
				$("#message").text("Deck set to " + deckName);
			}
		});	
	}
});



