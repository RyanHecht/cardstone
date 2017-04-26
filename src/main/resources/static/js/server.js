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

  sendChosen(id){
	 const payload = {"iid": id};
     console.log(id);
 	 const obj = {"type": MESSAGE_TYPE.CHOOSE_RESPONSE, "payload": payload};
 	 this.socket.send(JSON.stringify(obj));
    console.log("sent choose reponse");
	}

    // cardSelected(cardDiv){
		// console.log(cardDiv);
		// // $(".card").removeClass("cardSelected");
		// // cardDiv.addClass("cardSelected");
		// // selectedCard = cardDiv.attr("id");
	// }

	// targetChosen(cardDiv){
		// let response = sendTargetResponse(cardDiv.getId);
		// // if(response.valid){
			// // inputState = IDLE;
		// // }
		// // else{
			// // actionFailed(response.message);
		// // }
	// }

  cardTargeted(cardID,targetID){
   const payload = {"iid1": cardID, "iid2": targetID};
	 const obj = {"type": MESSAGE_TYPE.TARGETED_CARD, "payload": payload};
	 this.websocket.send(JSON.stringify(obj));
   console.log("sent card targeted");
	}

	cardPlayed(cardID,zoneID){
		const payload = {"iid": cardID, "zoneID": zoneID};
 	 const obj = {"type": MESSAGE_TYPE.ATTEMPTED_TO_PLAY, "payload": payload};
 	 this.websocket.send(JSON.stringify(obj));
   console.log("sent card played");
	}

    //isself is a boolean
    playerTargeted(cardID,isSelf){
			const payload = {};
		 const obj = {"type": MESSAGE_TYPE.TARGETED_PLAYER, "payload": payload};
		 this.websocket.send(JSON.stringify(obj));
     console.log("sent player targeted");
    }

	constructor() {
		this.websocket = new WebSocket("ws://" + window.location.host + "/socket");
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






	cardClicked(cardDiv){
		console.log(cardDiv);
		if(isTurn){
			switch(inputState){
				case StateEnum.IDLE:
					//cardSelected(cardDiv);
					break;
				case StateEnum.TARGET_NEEDED:
					//targetChosen(cardDiv);
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







	messageReceived(message){
		switch(message.type){
			case MESSAGE_TYPE.BOARD_STATE:
				this.boardReceived(message.payload);
                break;
            case MESSAGE_TYPE.ACTION_BAD:
                this.badMessage(message.payload);
                break;
            case MESSAGE_TYPE.ANIMATION:
                this.animationEventReceived(message.payload);
                break;
            case MESSAGE_TYPE.CHOOSE_REQUEST:
                this.chooseFrom(message.payload);
                break;
            default:
                console.log("Unknown message type!");
		}
	}

    badMessage(message){
        alert(message.errorMessage);
    }

    animationEventReceived(message){
        switch(message.eventType){
            case "attacked":
                quedAnims.push(animationMaker.getAttackAnimation(message.id1, message.id2).create());
                break;
            case "damaged":
                quedAnims.push(animationMaker.getDamagedAnimation(message.id1).create());
                break;
            default:
                console.log("unknown animation type");
        }
    }


	chooseFrom(cards){
		$("#chooseOneAsk").modal('show');
        cardCache.repairCardList(cards);
		let collection = new chooseZone($("#chooseZoneDisplay"),cards);
		collection.forceRedrawLater(500);
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
        let joshPool = new manaPool(10,'');
        joshPool.setFire(5);
        joshPool.setAir(5);
        joshPool.setWater(3);
        joshPool.setEarth(6);
        joshPool.setBalance(4);
        let smallJoshPool = new manaPool(3,'&nbsp;');
        smallJoshPool.setEarth(1);
        smallJoshPool.setBalance(1);

        let smallSkyWhalePool = new manaPool(3,'&nbsp;');
        let bigWhalePool = new manaPool(3,'&nbsp;');

        smallSkyWhalePool.setWater(1);
        smallSkyWhalePool.setAir(1);
        bigWhalePool.setWater(4);
        let skyCost = new cost(20,smallSkyWhalePool);
        let joshCost = new cost(10,smallJoshPool);
        let whaleCost = new cost(30, bigWhalePool);


        josh = new creatureCard(6,joshCost, "Josh Pattiz", "Perform a long sequence of actions." +
            " These may include dancing, singing, or just generally having a good time." +
            "At the end of this sequence, win the game.", "images/creature.jpg", 5,6);
        let skyWhale = new creatureCard(8,skyCost, "Sky Whale", "Deal 3 damage", "images/magicSkyWhale.jpg", 2, 10);
        let whale = new creatureCard(7,whaleCost, "Sea Leviathan", "At the end of every turn, destroy all minions with less than 3 attack",
        "images/giantWhale.png",5,12);

        let purgePool = new manaPool(3,'&nbsp;');
        purgePool.setBalance(2);
        let purgeCost = new cost(30,purgePool);

        let purge = new spellCard(4,purgeCost, "Purge the Unbelievers", "Destroy all creatures with attack not equal to their health. Ordering: And distribute the destroyed minions stats among surviving minions.",
        "images/purge.jpg");

        let water = new elementCard(5,"water");
        let balance = new elementCard(15,"balance");
        let earth = new elementCard(16,"earth");
        let fire = new elementCard(17,"fire");
        let air = new elementCard(18,"air");

        josh.setState("cardCanAttack");
        fire.setState("cardCanPlay");
        let collection = [];
        for(let x = 0; x < 20; x++){
            collection.push(josh);
            collection.push(skyWhale);
            collection.push(whale);
            collection.push(purge);
            collection.push(water);
            collection.push(balance);
            collection.push(air);
        }
        return collection;
    }

}
