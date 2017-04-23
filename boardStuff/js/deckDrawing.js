let allCards;
let josh;
const CARDS_PER_PAGE = 24;
let pages;
let curPage;
let server;
let isReplayMode = false;
let collect;
let list;

function setupJosh(){
    let smallJoshPool = new manaPool(3,'&nbsp;');
	smallJoshPool.setEarth(1);
	smallJoshPool.setBalance(1);
    let joshCost = new cost(10,smallJoshPool);
    josh = new creatureCard(6,joshCost, "Josh Pattiz", "Perform a long sequence of actions." +
		" These may include dancing, singing, or just generally having a good time." +
		"At the end of this sequence, win the game.", "images/creature.jpg", 5,6);
    josh.setState("cardCanAttack");
}

function redrawAll(){
    collect.forceRedraw();
    list.draw();
}

function setPage(){
    collect.setCards(pages.get(curPage));
    redrawAll();
}

function pageRight(){
    console.log("paging");
    if(pages.has(curPage + 1)){
        curPage++;
        setPage();
    }
}

function pageLeft(){
    if(curPage > 0){
        curPage--;
        setPage();
    }
}

function setupPaging(){
    $(".pageLeft").click(function(){
        pageLeft();
    })
    $(".pageRight").click(function(){
        pageRight();
    })
}

$('document').ready(function(){
    setupJosh();
    allCards = [];
    server = new Server();
    allCards = server.requestCardCollection();
    pages = new Map();
    for(let x = 0; x * CARDS_PER_PAGE < allCards.length; x++){
        let mod = x * CARDS_PER_PAGE;
        let page = [];
        for(let y = 0; y + x*CARDS_PER_PAGE < allCards.length && y < CARDS_PER_PAGE; y++){
            page.push(allCards[mod + y]);
        }
        pages.set(x,page);
    }
    curPage = 0;
    collect = new cardCollectionDeck($('.collectionDisplay'),pages.get(curPage),allCards);
    collect.forceRedraw();
    list = new deckList($(".deckList"));
    $(window).resize(function() {
         redrawAll();
        
	});
    $(document).keypress(function(e) {
        console.log(e.which);
        if(e.which == 122){
			collect.pushCard(josh);
			redrawAll();
		}
        else if(e.which == 37){
            pageLeft();
        }
        else if(e.which == 39){
            pageRight();
        }
    });
    setupPaging();
})