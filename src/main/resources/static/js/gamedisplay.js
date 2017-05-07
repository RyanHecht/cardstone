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
	
	const postParams = {user: $.cookie("id")};
	const gamesList = $("#game_table");
	const user = parseInt($.cookie("id"));
	$.post("user_replays", postParams, responseJSON => {
		const respObj = JSON.parse(responseJSON);
		console.log(respObj);
		
		for (let i = 0; i < respObj.length; i++) {
			const curr_game = respObj[i];
			
			const link = "/replay?id=" + curr_game.id;
			const oppId = curr_game.opponent;
			let winner;
			if (curr_game.winner == user) {
				winner = "You";
			} else if (curr_game.winner == oppId) {
				winner = "Them";
			} else if (curr_game.winner == 0) {
				winner = "Tie";
			}
			
			const postParams = {id: oppId};
			$.post("/username", postParams, responseJSON => {
				const opponent = JSON.parse(responseJSON).username;
				
				gamesList.append("<tr data-href='"  + link + "'>" + 					"<td>" + winner + "</td> <td> " + opponent + "</td> " + 
								"<td>" + curr_game.moves + "</td>" + 
								"<td>" + curr_game.date + "</td></tr>");
			});			
		}
	});

	gamesList.on('click', 'tr', function() {
		console.log("I was clickeddd");
		console.log($(this).data("href"));
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