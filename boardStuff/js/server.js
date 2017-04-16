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
		//parseMessage;
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

}