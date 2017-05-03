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
ID_RESPONSE: 13,
TURN_END: 14,
TEXT_MESSAGE: 15,
PLAYER_SEND_CHAT: 16,
RECEIVE_CHAT: 17,
TURN_START: 18,
SET_SPECTATOR: 19
};

class Server{

  sendChosen(id){
	 const payload = {"IID1": id};
     console.log(id);
 	 const obj = {"type": MESSAGE_TYPE.CHOOSE_RESPONSE, "payload": payload};
 	 this.websocket.send(JSON.stringify(obj));
    console.log("sent choose reponse");
	}

    endTurn(){
      const payload = {};
      const obj = {"type": MESSAGE_TYPE.TURN_END, "payload": payload};
    	this.websocket.send(JSON.stringify(obj));
       console.log("sent turn end");
    }

    // cardSelected(cardDiv){
		// console.log(cardDiv);
		// // $(".card").removeClass("cardSelected");
		// // cardDiv.addClass("cardSelected");
		// // selectedCard = cardDiv.attr("id");
	// }

    cardClicked(id){
        if(inputState == StateEnum.IDLE){

        }
        else if(inputState == StateEnum.TARGET_NEEDED){
            this.targetChosen(id);
        }
    }

	targetChosen(id){
		this.sendTargetResponse(id);
        inputState = StateEnum.IDLE;
	}


    //RY GUY WRITE THIS QUIK
    sendTargetResponse(id){
        console.log(id);
    }

  cardTargeted(cardID,targetID){
      console.log(cardID, targetID);
   const payload = {"IID1": cardID, "IID2": targetID};
	 const obj = {"type": MESSAGE_TYPE.TARGETED_CARD, "payload": payload};
	 this.websocket.send(JSON.stringify(obj));
   console.log("sent card targeted");
	}

	cardPlayed(cardID,zoneID){
		const payload = {"IID1": cardID, "zoneID": zoneID};
 	 const obj = {"type": MESSAGE_TYPE.ATTEMPTED_TO_PLAY, "payload": payload};
 	 this.websocket.send(JSON.stringify(obj));
   console.log("sent card played");
	}

    //isself is a boolean
    playerTargeted(cardID,isSelf){
        console.log("sent player targeted");
        const payload = {"IID1": cardID, "self": isSelf};
		const obj = {"type": MESSAGE_TYPE.TARGETED_PLAYER, "payload": payload};
		this.websocket.send(JSON.stringify(obj));
    }

	constructor() {
		this.websocket = new WebSocket("ws://" + window.location.host + "/socket");
		this.websocket.server = this;
		this.websocket.socket = this.websocket;
		this.websocket.onmessage = this.onWebSocketMessage;
		this.websocket.onopen = this.onWebSocketOpen;

	}

	onWebSocketOpen() {
		const payload = {"id": $.cookie("id")};
        const obj = {"type": MESSAGE_TYPE.ID_RESPONSE, "payload": payload}
		this.socket.send(JSON.stringify(obj));
		console.log('opened')
	}

	onWebSocketMessage(event) {
		this.server.messageReceived(JSON.parse(event.data));
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

    chooseTarget(){
        alert("please pick a target");
        inputState = StateEnum.TARGET_NEEDED;
    }

	messageReceived(message){
        if(!spectator){
            switch(message.type){
                case MESSAGE_TYPE.BOARD_STATE:
                    this.boardReceived(message.payload);
                    break;
                case MESSAGE_TYPE.ACTION_BAD:
                    this.badMessage(message.payload);
                    break;
                case MESSAGE_TYPE.ANIMATION://
                    this.animationEventReceived(message.payload);
                    break;
                case MESSAGE_TYPE.CHOOSE_REQUEST:
                    this.chooseFrom(message.payload.cards);
                    console.log(message);
                    break;
                case MESSAGE_TYPE.TARGET_REQUEST:
                    this.chooseTarget();
                    break;
                case MESSAGE_TYPE.TEXT_MESSAGE:
                    this.alertMessage(message.payload);
                    break;
                case MESSAGE_TYPE.RECEIVE_CHAT:
                    this.handleChat(message.payload);
                    break;
                case MESSAGE_TYPE.TURN_START://
                    turnTimer.startTurn(message.payload.isSelf);//
                    break;
                case MESSAGE_TYPE.SET_SPECTATOR:
                    spectator = true;
                    canAct = false;
                    spectating = message.payload.watching;
                    console.log("i'm a spectator");
                    break;
                default:
                    console.log("Unknown message type: " + message.type);
            }
        }
        else{
          console.log("i'm a spectator and i got a message of type " + message.type);
            switch(message.type){
                case MESSAGE_TYPE.BOARD_STATE:
                    this.boardReceived(message.payload);
                    break;
                case MESSAGE_TYPE.TURN_START://
                    turnTimer.startTurn(message.payload.isSelf);//
                    break;
                case MESSAGE_TYPE.ANIMATION://
                    console.log(message);
                    this.animationEventReceived(message.payload);
                    break;


            }
        }
	}



  alertMessage(message) {
    customAlert(message.message);
  }

  handleChat(message,flag) {
      if(flag == null){
          flag = false;
      }
    const chat = message.message;
    if(flag){
        $(".chatLog").append("<div class='chatMessage chatMessageMe alert alert-info'>" + chat + "</div>")
    }
    else{
         $(".chatLog").append("<div class='chatMessage chatMessageOther alert alert-success'>" + chat + "</div>")

    }
    $('.chatLog').scrollTop($('.chatLog')[0].scrollHeight);
    console.log("Got chat message: " + chat);
  }

  sendChat(chat) {
    const obj = {"type": MESSAGE_TYPE.PLAYER_SEND_CHAT, "payload": {"message": chat}};
    this.websocket.send(JSON.stringify(obj));
    this.handleChat(obj.payload,true);
  }

    badMessage(message){
        customAlert(message.message);
    }

    animationEventReceived(message){
        console.log(message);
        switch(message.eventType){
            case "creatureAttacked":
                quedAnims.push(animationsMaker.getAttackAnimation(message.id1, message.id2).create());
                break;
            case "creatureDamaged":
                quedAnims.push(animationsMaker.getDamagedAnimation(message.id1).create());
                break;
            case "playerAttacked":
                if(message.target == $.cookie("id")){
                    quedAnims.push(animationsMaker.getPlayerAttackedAnimation(message.id1,true).create());
                }
                else{
                    quedAnims.push(animationsMaker.getPlayerAttackedAnimation(message.id1,false).create());
                }
                break;
            case "playerDamaged":
                if(message.playerId == $.cookie("id")){
                    quedAnims.push(animationsMaker.getPlayerDamagedAnimation(true).create());
                }
                else{
                    quedAnims.push(animationsMaker.getPlayerDamagedAnimation(false).create());
                }
                break;
            case "cardDrawn":
                if(message.playerId == $.cookie("id")){
                    quedAnims.push(animationsMaker.getDrawnCardAnimation(true).create());
                }
                else{
                    quedAnims.push(animationsMaker.getDrawnCardAnimation(false).create());
                }
                break;
            case "cardPlayed":
                animationMaker.playCardAnimation(message.card);
                break;
            case "cardDied":
                quedAnims.push(animationsMaker.getDeadAnimation(message.id1).create());
                break;
            default:
                console.log("unknown animation type");
        }
    }


	chooseFrom(cards){
        console.log(cards);
		$("#chooseOneAsk").modal('show');
        cardCache.repairCardList(cards);
		let collection = new chooseZone($("#chooseZoneDisplay"),cards);
		collection.forceRedrawLater(500);
	}



	setPlayers(player1,player2){
		console.log(player1)
		wholeBoard.changeFeature("p1Health",player1.health);
		wholeBoard.changeFeature("p1RegRes",player1.resources);
		wholeBoard.changeFeature("p1Mana",manaPool.buildPool(1,'',player1.element));
		wholeBoard.changeFeature("p2Health",player2.health);
		wholeBoard.changeFeature("p2RegRes",player2.resources);
		wholeBoard.changeFeature("p2Mana",manaPool.buildPool(1,'',player2.element));
	}

	boardReceived(data){
        console.log(data);
        if(animations.length != 0 || quedAnims.length != 0){
            let $this = this;
            window.setTimeout(function(){
                $this.boardReceived(data)
            }, UPDATE_RATE);
            return;
        }
        if(spectator){
            if(data.player1.playerId != spectating){
                wholeBoard.flipTry();
            }
        }
        else{
            if(data.player1.playerId != parseInt($.cookie("id"))){
                wholeBoard.flipTry();
            }
        }
		this.setPlayers(data.player1,data.player2);
		wholeBoard.changeFeature("p1Deck",data.board.deckOne);
		wholeBoard.changeFeature("p2Deck",data.board.deckTwo);
        if(spectator){
            if(data.player1.playerId != spectating){
                wholeBoard.flipFeatures();
            }
        }
        else{
        if(data.player1.playerId != parseInt($.cookie("id"))){
            wholeBoard.flipFeatures();
        }
        }
        wholeBoard.buildResZones();
		cardCache.repairFrom(data.board);
		wholeBoard.getFromCache(data.board);
        console.log(data.player1, $.cookie("id"));

		redrawAll();
	}

    replayRequestStepBack(){

    }

    replayRequestStepForward(){

    }

    requestCardCollection(callback){
        let $this = this;
        $.get("/all_cards",function(responseJSON){
            const obj = JSON.parse(responseJSON);
            $this.receiveCardCollection(obj);
        })
    }

    receiveCardCollection(collection){
        console.log(collection);
        cardCache.repairCardList(collection);
        for(let card of collection){
            allCards.push(cardCache.getByIID(card.id));
        }
		allCardsReady();
    }
}
