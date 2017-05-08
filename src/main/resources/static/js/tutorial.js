const TutorialEnum = {
  INACTIVE: -1,
  MENU: 0,
  LOBBIES: 1,
  IN_LOBBY: 2,
  DECK_CHOSEN: 3,
  IN_GAME: 4,
  GAME_DONE: 5,
  REPLAY_PAGE: 6,
  REPLAY_DONE: 7,
  DECKS_PAGE: 8,
  BUILDING_DECK: 9,
  TUTORIAL_DONE: 10
};

function tutorialStage() {
	const stage = parseInt($.cookie("tutorial"));
	console.log("At stage " + stage);
	return stage;
}

class TutorialHandler {
	constructor(stage, user_prompt, next_stage, disabled_inputs) {
		this.stage = stage;
		this.user_prompt = user_prompt;
		this.next_stage = next_stage;
		this.disabled_inputs = disabled_inputs;
		this.disabled_inputs();
		
		if (stage != TutorialEnum.TUTORIAL_DONE) {
			$("#navbar").find("a").removeAttr("href");
			$("#logolink").removeAttr("href");
		}
	}
	
	action_complete() {
		$.cookie("tutorial", tutorialStage() + 1);
		this.next_stage();
	}
	
	user_lost(current_stage) {
		console.log("user lost at stage " + current_stage);
		$("#messageModal").modal("show");
		$("#messageheader").text("Are you lost?");
		$("#message").text("Close this modal to get back on track.");
		$("#messageModal").on("hide.bs.modal", function() {
			switch(current_stage) {
				case TutorialEnum.MENU:
					window.location.replace("/menu");
					break;
				case TutorialEnum.LOBBIES:
					window.location.replace("/lobbies");
					break;
				case TutorialEnum.IN_LOBBY:
					window.location.replace("/tutorial_lobby");
					break;
				case TutorialEnum.DECK_CHOSEN:
					$.cookie("tutorial", TutorialEnum.IN_LOBBY);
					window.location.replace("/tutorial_lobby");
					break;
				case TutorialEnum.IN_GAME:
					window.location.replace("/tutorialGame");
					break;
				case TutorialEnum.GAME_DONE:
					window.location.replace("/menu");
					break;
				case TutorialEnum.REPLAY_PAGE:
					window.location.replace("/replays");
					break;
				case TutorialEnum.REPLAY_DONE:
					window.location.replace("/menu");
					break;
				case TutorialEnum.DECKS_PAGE:
					window.location.replace("/decks.html");
					break;
				case TutorialEnum.BUILDING_DECK:
					window.location.replace("/deckDraw.html");
					break;
				case TutorialEnum.TUTORIAL_DONE:
					window.location.replace("/decks");
					break;
			}
		});
	}
	
	handle(current_stage) {
		console.log(current_stage);
		console.log(current_stage == this.stage);
		
		if (current_stage == this.stage) {
			this.user_prompt();
		} else {
			this.user_lost(current_stage);
		}
	}
}