let tutorialHandler;
const stage = TutorialEnum.DECKS_PAGE;
const end = TutorialEnum.TUTORIAL_DONE;
$(document).ready(() => {
	const tutorial_stage = tutorialStage();
	if (tutorial_stage >= 0) {
		switch (tutorial_stage) {
			case TutorialEnum.DECKS_PAGE:
				tutorialHandler = new TutorialHandler(stage, user_prompt, next_stage, disabled_inputs);		
				break;
			case TutorialEnum.TUTORIAL_DONE:
				tutorialHandler = new TutorialHandler(end, end_prompt, end_stage, disable_none);	
				break;
		}

		tutorialHandler.handle(tutorial_stage);
		$("#create_deck").on("click", function() {
			if (tutorial_stage == TutorialEnum.DECKS_PAGE) {
				tutorialHandler.action_complete();
			}
		});

		$("#messageModal").on("hide.bs.modal", function() {
			if (tutorial_stage == TutorialEnum.TUTORIAL_DONE) {
				tutorialHandler.action_complete();
			}
		});
	}
});

function user_prompt() {
	$("#messageModal").modal("show");
	$("#messageheader").text("Almost done!");
	$("#message").text("This page holds your saved decks. At any time, you can create a new deck, or edit pre-existing ones. These decks will then be available when you're choosing a deck in a lobby. Let's now try to build a deck of our own by clicking on the link below!");
};

function disabled_inputs() {
	$("#deck_list").find("a").removeAttr("href");
	$("#deck_list").find("a").css( 'pointer-events', 'none' );
};

function next_stage() {
	window.location.replace("/deckDraw.html");
};

function end_prompt() {
	$("#messageModal").modal("show");
	$("#messageheader").text("Congrats on finishing the tutorial!");
	$("#message").text("You're well on your way to becoming a seasoned Cardstone player! Now head off and kick some ass!");
};

function disable_none() {
};

function end_stage() {
	$.cookie("tutorial", -1);
};