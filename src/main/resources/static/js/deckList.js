class deckList extends drawableZone{
    
    constructor(div,allCards){
        super();
        this.div = div;
        this.deck = [];
        this.allCards = allCards;
    }
   
    fillWithNames(names){
        for(let name of names){
            let added = false;
            for(let card of allCards){
                if(card.name == name){
                    this.addCard(card);
                    added = true;
                    break;
                }
            }
            if(!added){
                alert("card " + name + " not found");
            }
        }
    }
   
    addCard(card){
        if(this.getNumCards() >= DECK_SIZE){
            alert("Deck can only hold " + DECK_SIZE + " cards. Remove some first!");
            return;
        }
        console.log(this.getNumCards());
        if(this.hasCard(card)){
            this.getWrapper(card).increment();
        }
        else{
            let wrapper = new cardWrapper(card,this.drawCards);
            wrapper.increment();
            this.deck.push(wrapper);
        }
        this.sortDeck();
    }
    
    addCardByIID(IID){
        if(this.getNumCards() >= DECK_SIZE){
            alert("Deck can only hold " + DECK_SIZE + " cards. Remove some first!");
            return;
        }
        for(let card of allCards){
            if(card.IID == IID){
                this.addCard(card);
                return;
            }
        }
    }
    
    draw(){
        this.cleanWrappers();
        this.div.empty();
        for(let wrapper of this.deck){
            this.div.append("<div class='deckListRow'></div>")
            wrapper.draw(this.div.children().last());
        }
    }
    
    cleanWrappers(){
        for(let x = this.deck.length - 1; x >= 0; x--){
            if(this.deck[x].count < 1){
                this.deck.splice(x,1);
            }
        }
    }
    
    hasCard(card){
        for(let deckCard of this.deck){
            if(deckCard.card.IID == card.IID){
                return true;
            }
        }
    }
    
    getWrapper(card){
        for(let deckCard of this.deck){
            if(deckCard.card.IID == card.IID){
                return deckCard;
            }
        }
    }
    
    
    sortDeck(){
        this.deck.sort(function(card1,card2){
          if(card1.card.cost.regRes < card2.card.cost.regRes){
              return -1;
          }  
          else if(card1.card.cost.regRes > card2.card.cost.regRes){
              return 1;
          }
          return 0;
        })
    }
    
    getCardArray(){
        let result = [];
        for(let wrapper of this.deck){
            for(let x = 0; x < wrapper.count; x++){
                result.push(wrapper.card.name);
            }
        }
        return result;
    }
    
    getNumCards(){
        let result = 0;
        for(let wrapper of this.deck){
            result+=wrapper.count;
        }
        return result;
    }
}

class cardWrapper{
    constructor(card,onChanged){
        this.card = card;
        this.count = 0;
        this.onChanged = onChanged;
    }
    increment(){
        if(!(this.card instanceof elementCard)){
            if(this.count >= MAX_NON_ELEMENT){
                alert("Can't have more than " + MAX_NON_ELEMENT + " of a non-element card");
                return;
            }
        }
        this.count++;
    }
    decrement(){
        this.count--;
    }
    
    draw(div){
        div.append("<div class='decrementer' id='decrementer_"+this.card.IID+"'></div>");
        div.append("<div class='deck_card_name hasTooltip'>"+this.card.name+" : " + this.count + "</div>");
        div.append("<div class='bigCardBox'></div>")
        let boxDiv = div.children(".bigCardBox");
        this.card.drawBig(boxDiv);
        boxDiv.hide();
        div.append("<div class='incrementer' id='incrementer"+this.card.IID+"'></div>");
        this.prepareChangers();
        this.prepareToolTips(div);
    }
    
    prepareChangers(){
        let $this = this;
        $("#decrementer_"+this.card.IID).click(function(){
            $this.decrement();
            redrawAll();
        })
         $("#incrementer"+this.card.IID).click(function(){
            list.addCardByIID($this.card.IID);
            redrawAll();
         })
    }
    
    prepareToolTips(div){
		let maxHeight = $(document).height();
		let maxWidth = $(document).width();
		let height;
		if(maxHeight > 400){
			height = 350;
		}
		else{
			height = maxHeight * .8;
			$(".bigCardBox").css({
				fontSize : "10px",
				lineHeight : "90%"
			});
		}
		let width = height * WIDTH_RATIO;
		while(width > maxWidth){
			height -= 10;
			width = height * WIDTH_RATIO;
		}
		div.find('.hasTooltip').each(function() {
			$(this).qtip({
				content: {
					text: $(this).next('div') 
				},
				style: {
					height: height,
					width: width,
					classes: 'qtip-bootstrap',
				},
				position: { 
					viewport: $(window),
					adjust: {
						method: 'flipinvert shift'
					}
					
				}
			});
		});
	}
}