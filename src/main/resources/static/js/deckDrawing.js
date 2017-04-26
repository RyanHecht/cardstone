let allCards;
let josh;
const CARDS_PER_PAGE = 12;
let pages;
let curPage;
let server;
let isReplayMode = false;
let collect;
let list;




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
    const post_param = {
        name:$("#deckName").val(),
        deck: list.getCardArray()
    }
    $.post("/deck_upload",JSON.stringify(post_param),function(){
        window.location.replace("/decks");
    });
    
}

$('document').ready(function(){
    allCards = [];
    server = new Server();
    allCards = server.requestCardCollection();
    buildPages(allCards);
    curPage = 0;
    collect = new cardCollectionDeck($('.collectionDisplay'),pages.get(curPage),allCards);
    collect.forceRedraw();
    list = new deckList($(".deckList"));
    $(window).resize(function() {
         redrawAll();
        
	});
    
    setupPaging();
    setupInput();
})