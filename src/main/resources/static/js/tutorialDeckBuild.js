let tutorialHandler;
let isTutorial = false;
const stage = TutorialEnum.BUILDING_DECK;

$(document).ready(() => {
	const tutorial_stage = tutorialStage();
	if (tutorial_stage >= 0) {
		tutorialHandler = new TutorialHandler(stage, user_prompt, next_stage, disabled_inputs);
		tutorialHandler.handle(tutorial_stage);
		isTutorial = true;
	}

	$("#deckSubmit").on('click', function() {
		if (isTutorial) {
			tutorialHandler.action_complete();
		}
	});	
});

function user_prompt() {
	$("#messageModal").modal("show");
	$("#messageheader").text("You're in the home stretch!");
	$("#message").text("Here, you can build and name the decks that you will use in-game. Create a deck by clicking on the cards you want. On the right hand side, you can use the + and - buttons for quicker access. Once you're done, name it and submit!");
};

function disabled_inputs() {
};

function next_stage() {
};