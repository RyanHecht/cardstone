const board1 = '{"type":"BOARD_STATE","payload":{"player1":{"health":30,"resources":0,"element":{"fire":0,"water":0,"air":0,"earth":0,"balance":0}},"player2":{"health":30,"resources":0,"element":{"fire":0,"water":0,"air":0,"earth":0,"balance":0}},"board":{"deckOne":0,"deckTwo":0,"hand1":{"changed":true,"cards":[]},"hand2":{"changed":true,"cards":[]},"aura1":{"changed":true,"cards":[]},"aura2":{"changed":true,"cards":[]},"creature1":{"changed":true,"cards":[{"text":"Im the coolest creature around.","id":1,"name":"Stub McStubbington","image":"images/creature.jpg","changed":true,"cost":{"resources":10,"fire":1,"air":0,"water":1,"balance":0,"earth":1},"type":"creature","attack":10,"health":20,"damaged":true},{"text":"Im flying!","id":2,"name":"Sky Whale","image":"images/magicSkyWhale.jpg","changed":true,"cost":{"resources":30,"fire":0,"air":1,"water":2,"balance":0,"earth":0},"type":"creature","attack":5,"health":30,"damaged":true},{"text":"Im flying!","id":3,"name":"Sky Whale","image":"images/magicSkyWhale.jpg","changed":true,"cost":{"resources":30,"fire":0,"air":1,"water":2,"balance":0,"earth":0},"type":"creature","attack":5,"health":30,"damaged":true},{"text":"Im flying!","id":4,"name":"Sky Whale","image":"images/magicSkyWhale.jpg","changed":true,"cost":{"resources":30,"fire":0,"air":1,"water":2,"balance":0,"earth":0},"type":"creature","attack":5,"health":30,"damaged":true}]},"creature2":{"changed":true,"cards":[{"text":"Im the coolest creature around.","id":5,"name":"Stub McStubbington","image":"images/creature.jpg","changed":true,"cost":{"resources":10,"fire":1,"air":0,"water":1,"balance":0,"earth":0},"type":"creature","attack":10,"health":20,"damaged":true},{"text":"Im flying!","id":6,"name":"Sky Whale","image":"images/magicSkyWhale.jpg","changed":true,"cost":{"resources":30,"fire":0,"air":1,"water":2,"balance":0,"earth":0},"type":"creature","attack":5,"health":30,"damaged":true}]}}}}'
let animations = [];
let quedAnims = [];
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
let cardCache;
let isReplayMode = false;
let mouseSystem;
let canvasLine = new DrawnLine(0,0,0,0);
let turnTimer;
let tooltipDisplay = true;
let spectator = false;
let canAct = true;
let spectating = 0;
let replayStep = 0;
let playedQueue = new PlayedQueue;

function redrawAll(){
	redrawChanged();
}

function resizeRedrawAll(){
    wholeBoard.forceRedraw();
    mouseSystem.redraw();
	setupCanvas();
    playedQueue.redraw();
}

function redrawChanged(){
    wholeBoard.draw();
    mouseSystem.redraw();
    playedQueue.redraw();
}

//I don't know how to kill elements from an array
function updateAndDrawAnimations(){
	canvasCtx.clearRect(0,0,canvasCtx.canvas.width,canvasCtx.canvas.height);
	for(let x = 0; x < animations.length; x++){
		if(animations[x].length == 0){
			animations.splice(x,1);
			continue;
		}

		for(let y = 0; y < animations[x].length; y++){
			animations[x][y].draw(canvasCtx);
			if(animations[x][y].update(animations[x][y],UPDATE_RATE)){
				animations[x].splice(y,1);
			}
		}
	}
	if(animations.length <= 0){
		if(quedAnims.length > 0){
            let popped = popFirst(quedAnims);
			animations.push(popped);
		}
	}
    if(mouseSystem.isClicked){
        canvasCtx.lineWidth = 4;
        canvasCtx.strokeStyle = "#ADD8E6";
        canvasCtx.shadowColor = "white";
        canvasCtx.shadowBlur = 10;
        canvasLine.draw(canvasCtx);
    }
	window.setTimeout(updateAndDrawAnimations, UPDATE_RATE);
}

function popFirst(arr){
    let result = arr[0];
    arr.splice(0,1);
    return result;
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
    canvas.width = $(document).width();
    canvas.height = $(document).height();
	$(".boardOverlay").attr('width',canvas.width);
};

function customAlert(message){
    $("#customMessage").text(message);
    $("#messageModal").modal("show");
}

function setupBoard(){
	let joshPool = new manaPool(1,'');
	joshPool.setFire(0);
	joshPool.setAir(0);
	// let smallJoshPool = new manaPool(3,'&nbsp;');
	// //smallJoshPool.setEarth(1);
    // smallJoshPool.setAir(1);

	// let smallSkyWhalePool = new manaPool(3,'&nbsp;');
	// let bigWhalePool = new manaPool(3,'&nbsp;');

	// smallSkyWhalePool.setWater(1);
	// smallSkyWhalePool.setAir(1);
	// bigWhalePool.setWater(4);
	// let skyCost = new cost(20,smallSkyWhalePool);
	// let joshCost = new cost(10,smallJoshPool);
	// let whaleCost = new cost(30, bigWhalePool);


	// josh = new creatureCard(-6,joshCost, "Josh Pattiz", "Perform a long sequence of actions." +
		// " These may include dancing, singing, or just generally having a good time." +
		// "At the end of this sequence, win the game.", "images/creature.jpg", 5,6);
    // let stateList1 = [];

    // stateList1.push("taunt");
    // josh.setStates(stateList1);
    // let stateList2 = [];
    // stateList2.push("canAttack");
    // let skyWhale = new creatureCard(-5,skyCost, "Sky Whale", "Deal 3 damage", "images/magicSkyWhale.jpg", 2, 10);
	// skyWhale.setStates(stateList2);
    // let whale = new creatureCard(-20,whaleCost, "Sea Leviathan", "At the end of every turn, destroy all minions with less than 3 attack",
	// "images/giantWhale.png",5,12);

	// let purgePool = new manaPool(3,'&nbsp;');
	// purgePool.setBalance(2);
	// let purgeCost = new cost(30,purgePool);

	// let purge = new spellCard(-4,purgeCost, "Purge the Unbelievers", "Destroy all creatures with attack not equal to their health. Ordering: And distribute the destroyed minions stats among surviving minions.",
	// "images/purge.jpg");

	// let water = new elementCard(-55,"water");
	// let balance = new elementCard(-15,"balance");
	// let earth = new elementCard(-16,"earth");
	// let fire = new elementCard(-17,"fire");
	// let air = new elementCard(-18,"air");

	// let back = new cardBack(-121);

	// let hand1Joshs = [fire, water, earth, air, balance, purge];
	// let hand2Joshs = [back,back,back,back,back];

	// let aura1Joshs = [whale, skyWhale, skyWhale, skyWhale, skyWhale];
	// let aura2Joshs = [whale, whale, whale, whale, whale];

	// let creature1Joshs = [josh, whale, skyWhale, skyWhale, whale];
	// let creature2Joshs = [whale, whale, skyWhale, skyWhale, whale];
    // cardCache.addNewParsedCard(josh);
    // cardCache.addNewParsedCard(whale);
    // cardCache.addNewParsedCard(skyWhale);
    // cardCache.addNewParsedCard(back);
    // cardCache.addNewParsedCard(fire);
    // cardCache.addNewParsedCard(water);
    // cardCache.addNewParsedCard(earth);
    // cardCache.addNewParsedCard(air);
    // cardCache.addNewParsedCard(balance);
    // cardCache.addNewParsedCard(purge);

	wholeBoard = new board([],[],[],[],[],[],30,30,10,10,joshPool,joshPool,30,30);
}

function setupServer(){
	server = new Server();
}



function setupMouseListen(){
    mouseSystem = new MouseManagerSystem();
    $(document).mousemove(function(event){
      mouseSystem.mousemoved(event);  
    });
    $(document).mouseup(function(event){
       mouseSystem.mouseup(event); 
    });
}

function prepTurnTimers(){
    turnTimer = new TurnTimer(true,100);
}

function popOptionsMenu(){
    $("#optionsMenu").modal("show");
}

function setupOptionsMenu(){
    $("#tooltipsToggle").change(function() {
        tooltipDisplay = this.checked;
        redrawAll();
    });
}

function setupKeypress(){
    	$(document).keyup(function(e) {
        if(canAct){
            if(e.which == 32) {
                $('#endTurnAsk').modal('show');
            }
        }
		if(e.which == 13) {
			server.chooseFrom([josh, josh, josh,josh, josh, josh,josh, josh, josh,josh, josh, josh,josh, josh, josh,
			josh, josh, josh,josh, josh, josh,josh, josh, josh,josh, josh, josh,josh, josh, josh,josh, josh, josh,josh, josh, josh,
			josh, josh, josh]);
		}
		else if(e.which == 122){
			console.log("dong");
			wholeBoard.pushCard(josh,ZoneEnum.CREATURE,1);
			redrawChanged();
		}
		else if(e.which == 88){
            quedAnims.push(animationsMaker.getDrawnCardAnimation(true).create());
			quedAnims.push(animationsMaker.getDrawnCardAnimation(false).create());
		}
        else if(e.which == 27){
            popOptionsMenu();
        }
        if(isReplay){
            if(e.which == 37){
                server.replayRequestStepBack();
            }
            else if(e.which == 39){
                server.replayRequestStepForward();
            }
        }
		console.log(e.which);
	});
}

function submitChat(){
    let cont = $("#chatMessageContent")
    server.sendChat(cont.val());
    cont.val("");
    
}

function setupChat(){
    $("#chatMessageContent").keyup(function(e){
        if(e.which == 13){
            submitChat();
        }
        e.stopPropagation();
    });
    $("#chatMessageSubmit").click(submitChat);
}


$(document).ready(function(){

    setupChat();
    setupMouseListen();
	setupCanvas();
    cardCache = new cardCacher();
	setupBoard();
    setupKeypress();
	$(window).resize(function() {
		clearAnimations();
		resizeRedrawAll();
	});
     $(".endTurnButton").click(function(){
            server.endTurn();
            $("#endTurnAsk").modal("hide");
    });
	updateAndDrawAnimations();
	wholeBoard.draw();
    prepTurnTimers();
    setupServer();
    setupOptionsMenu();
    $(".boxOuter").addClass("cursorTarget");
    mouseSystem.redraw();
    resizeRedrawAll();
    window.onbeforeunload = function() {
        return "Do you really want to leave the page? Doing so will end the game";
    };
    if(isReplay){
        canAct = false;
    }
});