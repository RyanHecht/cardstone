let tutorialHandler;
let isTutorial = false;
const stage = TutorialEnum.REPLAY_PAGE;

$(document).ready(() => {
	const tutorial_stage = tutorialStage();
	if (tutorial_stage >= 0) {
		tutorialHandler = new TutorialHandler(stage, user_prompt, next_stage, disabled_inputs);
		tutorialHandler.handle(tutorial_stage);
		isTutorial = true;
	}

	$(".clickable-row").on('click', function() {
		if (isTutorial) {
			tutorialHandler.action_complete();
		}
		window.location.replace($(this).data("href"));
	});	
});

function user_prompt() {
	$("#messageModal").modal("show");
	$("#messageheader").text("Soon you'll be a pro!");
	$("#message").text("This page holds your past games. You can see information on who won, how many turns there were, and when the game was played. Going into replay mode gives you an omnipotent view of the past game's board. When there, use the left and right arrow keys to step backwards and forewards through the game. Try clicking on a row of the table to replay the game you just played!");
};

function disabled_inputs() {
};

function next_stage() {
};