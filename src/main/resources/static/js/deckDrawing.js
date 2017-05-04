let allCards;
let josh;
const CARDS_PER_PAGE = 100;
let pages;
let curPage;
let server;
let isReplayMode = false;
let collect;
let list;
let DECK_SIZE = 50;
let MAX_NON_ELEMENT = 4;
let cardCache = new cardCacher();




function redrawAll(){
    collect.forceRedraw();
    list.draw();
    drawProgressBar();
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

function buildPages(cards){
    pages = new Map();
    for(let x = 0; x * CARDS_PER_PAGE < cards.length; x++){
        let mod = x * CARDS_PER_PAGE;
        let page = [];
        for(let y = 0; y + x*CARDS_PER_PAGE < cards.length && y < CARDS_PER_PAGE; y++){
            page.push(cards[mod + y]);
        }
        pages.set(x,page);
    }
}

function filterCollection(filterText){
    let filteredCards = filter(allCards,filterText);
    buildPages(filteredCards);
    curPage = 0;
    collect.setCards(pages.get(curPage));
    redrawAll();
}

function filter(cards,filterBy){
    let result = [];
    filterBy = filterBy.toLowerCase();
    for(let card of cards){
        if(card.name.toLowerCase().includes(filterBy)){
            result.push(card);
            continue;
        }
        if(card.text != null){
            if(card.text.toLowerCase().includes(filterBy)){
                result.push(card);
                continue;
            }
        }
        if(card.type != null){
            if(card.type.includes(filterBy)){
                result.push(card);
                continue;
            }
        }
    }
    return result;
}

function setupInput(){
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
    $("#filterSubmit").click(function(){
       filterCollection($("#filterText").val()); 
    });
    $("#deckSubmit").click(function(){
        submitDeck();
    })
}

function submitDeck(){
	console.log($("#deckName").val());
    const post_params = {
        name:$("#deckName").val(),
        deck: JSON.stringify(list.getCardArray())
    }
    $.post("/deck_upload",post_params, responseJSON => {
		console.log(post_params.name);
		console.log(post_params.deck);
        window.location.replace("/decks");
    });
    
}

function drawProgressBar(){
    let numCards = list.getNumCards();
    $("#progressBar").css("width",((numCards * 100 / DECK_SIZE ) + "%"))
    if(numCards == DECK_SIZE){
        $("#progressBar").css("border-color","lightyellow");
    }
    else{
        $("#progressBar").css("border-color","lightblue");
    }
}

function checkForGlobals(){
     if(typeof cardNames != "undefined"){
        list.fillWithNames(cardNames);
        $("#deckName").val(nameOfDeck);
		list.draw();
    }
}

function allCardsReady(){
	    buildPages(allCards);
	

    curPage = 0;
    collect = new cardCollectionDeck($('.collectionDisplay'),pages.get(curPage),allCards);
    list = new deckList($(".deckList"));
    $(window).resize(function() {
         redrawAll();
        
	});
    setupPaging();
    setupInput();
    redrawAll();
	checkForGlobals();
    
}

$('document').ready(function(){
    allCards = [];
    server = new Server();
    server.requestCardCollection();
})