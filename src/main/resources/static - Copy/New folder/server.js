class Server{
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
			case "BOARD_STATE":
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
		wholeBoard.changeFeature("p1Health",player1.health);
		wholeBoard.changeFeature("p1RegRes",player1.resources);
		wholeBoard.changeFeature("p1Mana",manaPool.buildPool(10,'',player1.element));
		wholeBoard.changeFeature("p2Health",player2.health);
		wholeBoard.changeFeature("p2RegRes",player2.resources);
		wholeBoard.changeFeature("p2Mana",manaPool.buildPool(10,'',player2.element));
	}	
	
	boardReceived(data){
		this.setPlayers(data.player1,data.player2);
		wholeBoard.changeFeature("p1Deck",data.board.deckOne);
		wholeBoard.changeFeature("p2Deck",data.board.deckTwo);
		cardCache.repairFrom(data.board);
		wholeBoard.getFromCache(data.board);
		redrawAll();
	}
	
}