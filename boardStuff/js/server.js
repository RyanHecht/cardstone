const MESSAGE_TYPE = {
BOARD_STATE: 0,
UNDERSTOOD_BOARD_STATE: 1,
ANIMATION: 2,
EXPLICIT_ANIMATION: 3,
TARGETED_CARD: 4,
TARGETED_PLAYER: 5,
ATTEMPTED_TO_PLAY: 6,
CHOOSE_REQUEST: 7,
CHOOSE_RESPONSE: 8,
TARGET_REQUEST: 9,
TARGET_RESPONSE: 10,
ACTION_OK: 11,
ACTION_BAD: 12,
ID_RESPONSE: 13
};

class Server{

	constructor() {
		this.websocket = new WebSocket("ws://localhost:8080/socket");
		this.websocket.server = this;
		this.websocket.socket = this.websocket;
		this.websocket.onmessage = this.onWebSocketMessage;
		this.websocket.onopen = this.onWebSocketOpen;

	}

	onWebSocketOpen() {
		const payload = {"id": 1};
        const obj = {"type": MESSAGE_TYPE.ID_RESPONSE, "payload": payload}
		this.socket.send(JSON.stringify(obj));
		console.log('opened')
	}

	onWebSocketMessage(event) {
		this.server.messageReceived(JSON.parse(event.data));
	}



	sendChosen(id){
		console.log("chose" + id);
	}


	cardClicked(cardDiv){
		console.log(cardDiv);
		if(isTurn){
			switch(inputState){
				case StateEnum.IDLE:
					cardSelected(cardDiv);
					break;
				case StateEnum.TARGET_NEEDED:
					targetChosen(cardDiv);
					break;
				default:
					actionFailed();
			}
		}
		else{
			actionFailed();
		}

	}

	parseAnimation(data){
		let animationType = data.animationType;
		let animation;
		switch(animationType){
			case AnimationEnum.RADIAL:
				animation = buildRadial(data);
				break;
			case AnimationEnum.CLOUD:
				animation = buildCloud(data);
				break;
		}
		animations.push(animation.create());
		updateAndDrawAnimations();
	}



	parseEvent(data){

	}




	cardSelected(cardDiv){
		console.log(cardDiv);
		// $(".card").removeClass("cardSelected");
		// cardDiv.addClass("cardSelected");
		// selectedCard = cardDiv.attr("id");
	}

	targetChosen(cardDiv){
		let response = sendTargetResponse(cardDiv.getId);
		// if(response.valid){
			// inputState = IDLE;
		// }
		// else{
			// actionFailed(response.message);
		// }
	}

	messageReceived(message){
		switch(message.type){
			case MESSAGE_TYPE.BOARD_STATE:
				console.log("received board: " + JSON.stringify(message.payload))
				this.boardReceived(message.payload);
		}
	}

	chooseFrom(cards){
		$("#chooseOneAsk").modal('show');
		let collection = new chooseZone($("#chooseZoneDisplay"),cards);
		collection.forceRedrawLater(2500);
	}

	cardTargeted(cardID,targetID){
		//
	}

	cardPlayed(cardID,zoneID){

	}

	setPlayers(player1,player2){
		console.log(player1)
		wholeBoard.changeFeature("p1Health",player1.health);
		wholeBoard.changeFeature("p1RegRes",player1.resources);
		wholeBoard.changeFeature("p1Mana",manaPool.buildPool(10,'',player1.element));
		wholeBoard.changeFeature("p2Health",player2.health);
		wholeBoard.changeFeature("p2RegRes",player2.resources);
		wholeBoard.changeFeature("p2Mana",manaPool.buildPool(10,'',player2.element));
	}

	boardReceived(data){
		console.log(data)
		this.setPlayers(data.player1,data.player2);
		wholeBoard.changeFeature("p1Deck",data.board.deckOne);
		wholeBoard.changeFeature("p2Deck",data.board.deckTwo);
		cardCache.repairFrom(data.board);
		wholeBoard.getFromCache(data.board);
		redrawAll();
	}
    
    replayRequestStepBack(){
        
    }
    
    replayRequestStepForward(){
        
    }
    
    requestCardCollection(){
        let collection = [];
        for(let x = 0; x < 100; x++){
            collection.push(josh);
        }
        return collection;
    }

}
