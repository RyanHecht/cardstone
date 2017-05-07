let tutorialHandler;
const tutorial_start = TutorialEnum.MENU;
const game_done = TutorialEnum.GAME_DONE;
const replay_done = TutorialEnum.REPLAY_DONE;

$(document).ready(() => {
	const tutorial_stage = tutorialStage();
	if (tutorial_stage >= 0) {
		switch (tutorial_stage) {
			case tutorial_start:
				tutorialHandler = new TutorialHandler(tutorial_start, start_prompt, start_next, start_disabled);
				break;
			case game_done:
				tutorialHandler = new TutorialHandler(game_done, postgame_prompt, postgame_next, postgame_disabled);
				break;
			case replay_done:
				tutorialHandler = new TutorialHandler(replay_done, postreplay_prompt, postreplay_next, postreplay_disabled);
				break;
			default:
				tutorialHandler = new TutorialHandler(tutorial_start, start_prompt, start_next, start_disabled);
				break;
		}
		
		tutorialHandler.handle(tutorial_stage);
		tutorialButtonBind("#lobbies", tutorial_start);
		tutorialButtonBind("#games", game_done);
		tutorialButtonBind("#decks", replay_done);
	}
});

function tutorialButtonBind(name, stage) {
	$(name).on("click", function() {
		if (tutorialStage() == stage) {
			tutorialHandler.action_complete();
		}		
	});
};

$("#skip_tutorial").click(function() {
	$.cookie("tutorial", -1);
//	$("#lobbies").removeClass("tutorialHighlight");
//	$("#games").removeClass("disabled");
//	$("#decks").removeClass("disabled");
	window.location.reload();
});

function start_prompt() {
	$("#startmodal").modal("show");
	$("#startheader").text("Welcome to Cardstone!");
	$("#startmessage").text("Cardstone is a card game. As one of our esteemed users, you can do 3 main things: create/edit decks to play with, replay your past games, and play or spectate in games. So follow us along for a quick tutorial, beginning with clicking the lobbies button on the page.");
	// maybe setInterval later for retarded users
	// prompt them every 30 seconds
	// could turn messages into method
};

function start_disabled() {
	$("#games").addClass("disabled");
	$("#decks").addClass("disabled");
	$("#lobbies").addClass("tutorialHighlight");
};

function start_next() {
	window.location.replace("/lobbies");
};

function postgame_prompt() {
	$("#messageModal").modal("show");
	$("#messageheader").text("Congrats on finishing your first game!");
	$("#message").text("Now click on the games button to see the past games you've played.");
	// maybe setInterval later for retarded users
	// prompt them every 30 seconds
	// could turn messages into method
};

function postgame_disabled() {
	$("#decks").addClass("disabled");
	$("#lobbies").addClass("disabled");
	$("#games").addClass("tutorialHighlight");
};

function postgame_next() {
	window.location.replace("/games");
};

function postreplay_prompt() {
	$("#messageModal").modal("show");
	$("#messageheader").text("Great job on learning from your first replay!");
	$("#message").text("Now let's build a deck so you can get started on your own!");
	// maybe setInterval later for retarded users
	// prompt them every 30 seconds
	// could turn messages into method
};

function postreplay_disabled() {
	$("#games").addClass("disabled");
	$("#lobbies").addClass("disabled");
	$("#decks").addClass("tutorialHighlight");
};

function postreplay_next() {
	window.location.replace("/decks");
};