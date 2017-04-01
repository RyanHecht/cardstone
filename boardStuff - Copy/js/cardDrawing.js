const WIDTH_RATIO = .7;

let isExpanded = false;
let wholeBoard;
/**
Fill a div with a list of cards, if they can fit.
Adds rows as needed up to max row count.
*/


	
function redrawAll(){
	console.log("readrawe");
	wholeBoard.forceRedraw();
}	
	
$('document').ready(function(){
	let josh = new card("Too Expensive", "Josh Pattiz", "Perform a long sequence of actions." + 
		" These may include dancing, singing, or just generally having a good time." + 
		"At the end of this sequence, win the game.", 5, "creature.jpg", "So many stats");
	let hand1Joshs = [josh, josh, josh, josh, josh];
	let hand2Joshs = [josh, josh, josh, josh, josh];

	let aura1Joshs = [josh, josh, josh, josh, josh];
	let aura2Joshs = [josh, josh, josh, josh, josh];
	
	let creature1Joshs = [josh, josh, josh, josh, josh];
	let creature2Joshs = [josh, josh, josh, josh, josh];
	wholeBoard = new board(hand1Joshs,hand2Joshs,aura1Joshs,aura2Joshs,creature1Joshs,creature2Joshs);
	$(document).keypress(function(e) {
		if(e.which == 13) {
			wholeBoard.pushCard(josh,ZoneEnum.CREATURE,2);
			wholeBoard.draw();
		}
	});
	 $(window).resize(function() {
		redrawAll();
	});

	wholeBoard.draw();

	
});