const WIDTH_RATIO = .7;
const STAT_ALPHA = .5;
const UPDATE_RATE = 25;

const fire = {r : 200, g : 15, b : 0};
const water = {r : 128, g : 188, b : 163};
const earth = {r : 130, g : 99, b : 66};
const air = {r : 246, g : 247, b : 189};

const fireText = "rgba(200,15,0," + STAT_ALPHA  + ")";
const waterText = "rgba(42,143,189," + STAT_ALPHA + ")";
const earthText = "rgba(168,120,72," + (STAT_ALPHA + .2) + ")";
const airText = "rgba(201,255,227," + STAT_ALPHA + ")";
const balanceText = "rgba(30,30,30," + (STAT_ALPHA) +  ")";
const DEFAULT_ANIM_COLOR = "rgba(100,100,100,1.0)"
let animations = [];
let canvasCtx;
let canvas;
let canvasQuery;
let expandedInUse = false;
let wholeBoard;
let time;
let date;
let isTurn = true;
let inputState = StateEnum.IDLE;
let selectedCard;
let josh;
let server;

/**
Fill a div with a list of cards, if they can fit.
Adds rows as needed up to max row count.
*/




	
function redrawAll(){
	wholeBoard.forceRedraw();
}	
	
//I don't know how to kill elements from an array
function updateAndDrawAnimations(){
	canvasQuery.show();
	canvasCtx.clearRect(0,0,canvasCtx.canvas.width,canvasCtx.canvas.height);
	for(let x = 0; x < animations.length; x++){
		if(animations[x].length == 0){
			animations.splice(x,1);
			if(animations.length == 0){
				canvasQuery.hide();
				return;
			}
			continue;
		}
		
		for(let y = 0; y < animations[x].length; y++){
			animations[x][y].draw(canvasCtx);
			if(animations[x][y].update(animations[x][y],UPDATE_RATE)){
				animations[x].splice(y,1);
			}
		}
	}
	window.setTimeout(updateAndDrawAnimations, UPDATE_RATE);
}	

function clearAnimations(){
	animations = [];
}
	
function setupCanvas(){
	canvas = $(".boardOverlay")[0];
	canvasQuery = $(".boardOverlay");
	let htmlBoard = $('.board');
	animations = [];
	canvasCtx = canvas.getContext('2d');
    canvas.width = htmlBoard.width();
    canvas.height = htmlBoard.height();
	$(".boardOverlay").attr('width',canvas.width);
	canvasQuery.hide();
};

function setupBoard(){
	let joshPool = new manaPool(10,'');
	joshPool.setFire(5);
	joshPool.setAir(5);
	joshPool.setWater(3);
	joshPool.setEarth(6);
	joshPool.setBalance(4);
	let smallJoshPool = new manaPool(3,'&nbsp;');
	smallJoshPool.setEarth(1);
	smallJoshPool.setBalance(1);
	
	let smallSkyWhalePool = new manaPool(3,'&nbsp;');
	let bigWhalePool = new manaPool(3,'&nbsp;');
	
	smallSkyWhalePool.setWater(1);
	smallSkyWhalePool.setAir(1);
	bigWhalePool.setWater(4);
	let skyCost = new cost(20,smallSkyWhalePool);
	let joshCost = new cost(10,smallJoshPool);
	let whaleCost = new cost(30, bigWhalePool);

	
	josh = new creatureCard(1,joshCost, "Josh Pattiz", "Perform a long sequence of actions." + 
		" These may include dancing, singing, or just generally having a good time." + 
		"At the end of this sequence, win the game.", "images/creature.jpg", 5,6);
	let skyWhale = new creatureCard(2,skyCost, "Sky Whale", "Deal 3 damage", "images/magicSkyWhale.jpg", 2, 10);
	let whale = new creatureCard(3,whaleCost, "Sea Leviathan", "At the end of every turn, destroy all minions with less than 3 attack", 
	"images/giantWhale.png",5,12);
	
	let purgePool = new manaPool(3,'&nbsp;');
	purgePool.setBalance(2);
	let purgeCost = new cost(30,purgePool);
	
	let purge = new spellCard(4,purgeCost, "Purge the Unbelievers", "Destroy all creatures with attack not equal to their health. Ordering: And distribute the destroyed minions stats among surviving minions.",
	"images/purge.jpg");
	
	let water = new elementCard(14,"water");
	let balance = new elementCard(15,"balance");
	let earth = new elementCard(16,"earth");
	let fire = new elementCard(17,"fire");
	let air = new elementCard(18,"air");
	
	josh.setState("cardCanAttack");
	fire.setState("cardCanPlay");
	let back = new cardBack(1);
	
	let hand1Joshs = [fire, water, earth, air, balance, purge];
	let hand2Joshs = [back,back,back,back,back];

	let aura1Joshs = [whale, skyWhale, skyWhale, skyWhale, skyWhale];
	let aura2Joshs = [whale, whale, whale, whale, skyWhale];
	
	let creature1Joshs = [josh, whale, skyWhale, skyWhale, whale];
	let creature2Joshs = [whale, whale, skyWhale, skyWhale, whale];
	
	wholeBoard = new board(hand1Joshs,hand2Joshs,aura1Joshs,aura2Joshs,creature1Joshs,creature2Joshs,20,30,10,15,joshPool,joshPool,30,30);
}





function buildRadial(options){
	let radial = new RadialAnimation();
	radial.buildFromOptions(options);
	return radial;
}

function buildCloud(options){
	let cloud = new linearAnimation();
	cloud.buildFromOptions(options);
	return cloud;
}



function setupServer(){
	server = new Server();
}	

function setupCardClick(){
	$(".card").on("click", function(){
		server.cardClicked($(this));
	});
}	
	
$(document).ready(function(){	
	setupCanvas();
	setupBoard();
	setupServer();
	$(document).keypress(function(e) {
		console.log("aqui");
		if(e.which == 13) {
			server.chooseFrom([josh, josh, josh,josh, josh, josh,josh, josh, josh,josh, josh, josh,josh, josh, josh,
			josh, josh, josh,josh, josh, josh,josh, josh, josh,josh, josh, josh,josh, josh, josh,josh, josh, josh,josh, josh, josh,
			josh, josh, josh]);
		}
		else if(e.which == 32) {
			$('#endTurnAsk').modal('show');
		}
		else if(e.which == 122){
			console.log("dong");
			wholeBoard.pushCard(josh,ZoneEnum.CREATURE,1);
			redrawAll();
		}
		console.log(e.which);
	});
	 $(window).resize(function() {
		clearAnimations();
		redrawAll();
	});
	wholeBoard.draw();
	setupCardClick();
});