let tutorialHandler;
const arrival = TutorialEnum.IN_LOBBY;
const deckChosen = TutorialEnum.DECK_CHOSEN;

$(document).ready(() => {
	const tutorial_stage = tutorialStage();
	if (tutorial_stage >= 0) {		
		switch (tutorial_stage) {
			case arrival:
				tutorialHandler = new TutorialHandler(arrival, arrival_prompt, after_arrival, arrival_disabled);
				break;
			case deckChosen:
				tutorialHandler = new TutorialHandler(deckChosen, deck_prompt, start_game, disabled_select);
				break;
			default:
				tutorialHandler = new TutorialHandler(arrival, arrival_prompt, after_arrival, arrival_disabled);
				break;				
		}
		tutorialHandler.handle(tutorial_stage);
	}
});

function arrival_prompt() {
	$("#messageModal").modal("show");
	$("#messageheader").text("Keep it up!");
	$("#message").text("This is the lobby page, the last stop before starting a game. You can get in a game once there are two players who have both set their decks. Right now, we're waiting on you! Set your deck using the dropdown so that we can start a game.");
};

function arrival_disabled() {
};

function after_arrival() {
	tutorialHandler = new TutorialHandler(deckChosen, deck_prompt, start_game, disabled_select);
};

function deck_prompt() {
	$("#messageModal").modal("show");
	$("#messageheader").text("Great!");
	$("#message").text("Now just click the play button to start the game!");
};

function start_game() {
	window.location.replace("/tutorialGame");
};

function disabled_select() {
	$("#deckselect").attr("disabled", "disabled");
};

$("#deckselect").on("change", function() {
	console.log("triggered");
	tutorialHandler.action_complete();
	$("#play").removeClass("disabled");
	tutorialHandler.handle(tutorialStage());
});

$("#play").on("click", function() {
	tutorialHandler.action_complete();
});