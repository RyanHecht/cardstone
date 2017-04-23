class deckList extends drawableZone{
    
    constructor(div,allCards){
        super();
        this.div = div;
        this.deck = [];
        this.allCards = allCards;
        
    }
   
    addCard(card){
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
        console.log(IID);
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
            if(deckCard.card.IID = card.IID){
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
}

class cardWrapper{
    constructor(card,onChanged){
        this.card = card;
        this.count = 0;
        this.onChanged = onChanged;
    }
    increment(){
        this.count++;
    }
    decrement(){
        this.count--;
    }
    
    draw(div){
        div.append("<div class='decrementer' id='decrementer_"+this.card.IID+"'></div>");
        div.append("<div class='deck_card_name'>"+this.card.name+" : " + this.count + "</div>");
        div.append("<div class='incrementer' id='incrementer"+this.card.IID+"'></div>");
        this.prepareChangers();
    }
    
    prepareChangers(){
        let $this = this;
        $("#decrementer_"+this.card.IID).click(function(){
            $this.decrement();
            redrawAll();
        })
         $("#incrementer"+this.card.IID).click(function(){
            $this.increment();
            redrawAll();
         })
    }
}