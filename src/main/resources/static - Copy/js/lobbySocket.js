const LOBBY_MESSAGE_TYPE = {
LOBBY_CONNECT: 0,
LOBBY_CREATE: 1
OPPONENT_ENTERED_LOBBY: 2,
OPPONENT_LEFT_LOBBY: 3,
SELF_SET_DECK: 4,
OPPONENT_SET_DECK: 5,
START_GAME_REQUEST: 6,
GAME_IS_STARTING: 7,
LOBBY_CANCELLED: 8
};

class LobbySocket {

	constructor(userId, isHost, onOpponentJoin, onOpponentLeave, onOpponentSetDeck, onGameStart, onLobbyCancel) {
		this.websocket = new WebSocket("ws://" + window.location.host + "/lobbySocket");
		this.websocket.server = this;
		this.websocket.socket = this.websocket;
		this.websocket.onmessage = this.onWebSocketMessage;
		this.websocket.onopen = this.onWebSocketOpen;
    this.server.onOpponentJoin = onOpponentJoin;
    this.server.onOpponentLeave = onOpponentLeave;
    this.server.onOpponentSetDeck = onOpponentSetDeck;
    this.server.onGameStart = onGameStart;
    this.server.onLobbyCancel = onLobbyCancel;
    this.server.isHost = isHost;
    this.server.userId = userId;
	}

	onWebSocketOpen() {
    const type = -1
    const payload = {"id": this.server.userId}
    if (isHost) {
      type = LOBBY_MESSAGE_TYPE.LOBBY_CREATE;
    } else {
      type = LOBBY_MESSAGE_TYPE.LOBBY_CONNECT;
    }

    const obj = {"type": type, "payload": payload};
		this.socket.send(JSON.stringify(obj));
		console.log('opened');
	}

	onWebSocketMessage(event) {
		this.server.messageReceived(JSON.parse(event.data));
	}

  setDeck(deck) {
    const obj = {"type": LOBBY_MESSAGE_TYPE.SELF_SET_DECK, "payload": deck};
    this.socket.send(JSON.stringify(obj));
    console.log("sent deck: " + deck);
  }

  startGame() {
    if (isHost) {
      const obj = {"type": LOBBY_MESSAGE_TYPE.START_GAME_REQUEST,
                    "payload": {}};
      this.socket.send(JSON.stringify(obj));
      console.log("start game request");
    }
  }

  messageReceived(message) {
    switch(message.type) {
      case LOBBY_MESSAGE_TYPE.OPPONENT_ENTERED_LOBBY:
        this.server.onOpponentJoin(message.payload.id);
        break;
      case LOBBY_MESSAGE_TYPE.OPPONENT_LEFT_LOBBY:
        this.server.onOpponentLeave();
        break;
      case LOBBY_MESSAGE_TYPE.OPPONENT_SET_DECK:
        this.server.onOpponentSetDeck(message.payload.name);
        break;
      case LOBBY_MESSAGE_TYPE.GAME_IS_STARTING:
        this.server.onGameStart();
        break;
      case LOBBY_MESSAGE_TYPE.LOBBY_CANCELLED:
        this.server.onLobbyCancel();
        break;
    }
  }






}
