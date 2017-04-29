let socket;

$(document).ready(() => {	
	socket = new LobbySocket(parseInt($.cookie("id")), isHost, onOpponentJoin, onOpponentLeave, onOpponentSetDeck, onGameStart, onLobbyCancel, handleChat);
});

function onOpponentJoin(opponentId) {
	console.log("Have opp id" + opponentId);
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



