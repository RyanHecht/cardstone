const LOBBY_MESSAGE_TYPE = {
	LOBBY_CONNECT: 0,
	LOBBY_CREATE: 1,
	OPPONENT_ENTERED_LOBBY: 2,
	OPPONENT_LEFT_LOBBY: 3,
	SELF_SET_DECK: 4,
	OPPONENT_SET_DECK: 5,
	START_GAME_REQUEST: 6,
	GAME_IS_STARTING: 7,
	LOBBY_CANCELLED: 8,
	PLAYER_SEND_CHAT: 9,
	RECEIVE_CHAT: 10
};

class LobbySocket {

	constructor(userId, isHost, onOpponentJoin, onOpponentLeave, onOpponentSetDeck, onGameStart, onLobbyCancel, handleChat) {
		this.websocket = new WebSocket("ws://" + window.location.host + "/lobbySocket");
		this.websocket.server = this;
		this.websocket.socket = this.websocket;
		this.websocket.onmessage = this.onWebSocketMessage;
		this.websocket.onopen = this.onWebSocketOpen;
		this.onOpponentJoin = onOpponentJoin;
		this.onOpponentLeave = onOpponentLeave;
		this.onOpponentSetDeck = onOpponentSetDeck;
		this.onGameStart = onGameStart;
		this.onLobbyCancel = onLobbyCancel;
		this.handleChat = handleChat;
		this.isHost = isHost;
		this.userId = userId;
	}

	onWebSocketOpen() {
		let type = -1
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
		console.log(event.data)
		this.server.messageReceived(JSON.parse(event.data));
	}

  sendChat(chat) {
    const obj = {"type": LOBBY_MESSAGE_TYPE.PLAYER_SEND_CHAT, "payload": {"message": chat}};
    this.websocket.send(JSON.stringify(obj));
    console.log("sent chat: " + chat);
  }

  setDeck(deck) {
    const obj = {"type": LOBBY_MESSAGE_TYPE.SELF_SET_DECK, "payload": deck};
    this.websocket.send(JSON.stringify(obj));
    console.log("sent deck: " + deck);
  }

  startGame() {
    if (isHost) {
      const obj = {"type": LOBBY_MESSAGE_TYPE.START_GAME_REQUEST,
                    "payload": {}};
      this.websocket.send(JSON.stringify(obj));
      console.log("start game request");
    }
  }

  messageReceived(message) {
		console.log(message);
    switch(message.type) {
      case LOBBY_MESSAGE_TYPE.OPPONENT_ENTERED_LOBBY:
        this.onOpponentJoin(message.payload.id);
        break;
      case LOBBY_MESSAGE_TYPE.OPPONENT_LEFT_LOBBY:
        this.onOpponentLeave();
        break;
      case LOBBY_MESSAGE_TYPE.OPPONENT_SET_DECK:
        this.onOpponentSetDeck(message.payload.name);
        break;
      case LOBBY_MESSAGE_TYPE.GAME_IS_STARTING:
        this.onGameStart();
        break;
      case LOBBY_MESSAGE_TYPE.LOBBY_CANCELLED:
        this.onLobbyCancel();
				break;
			case LOBBY_MESSAGE_TYPE.RECEIVE_CHAT:
				this.handleChat(message.message);
        break;
    }
  }
}
