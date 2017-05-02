class cardCacher{

	constructor(){
		this.cache = new Map();
	}
	
	getByIID(IID){
		if(this.cache.has(IID)){
			return this.cache.get(IID);
		}
	}
	
	setByIID(IID,newCard){
		this.cache.set(IID,newCard);
	}
	
	repairFrom(board){
		this.repairZone(board.hand1);
		this.repairZone(board.hand2);
		this.repairZone(board.aura1);
		this.repairZone(board.aura2);
		this.repairZone(board.creature1);
		this.repairZone(board.creature2);
	}
	
	repairZone(zone){
		if(!zone.changed){
			return;
		}
		for(let card of zone.cards){
			this.repairCard(card);
		}
	}
    
    repairCardList(cards){
        for(let card of cards){
            this.repairCard(card);
        }
    }
	
	repairCard(card){
		if(!card.changed){
			return;
		}
		else{
			this.addNewCard(card);
		}
	}

	modifyCard(card){
		this.cache.get(card.id).modifyWith(card);
	}
	
	addNewCard(card){
		switch(card.type){
			case "creature":
				this.addNewCreature(card);
				break;
            case "spell":
                this.addNewSpell(card);
                break;
            case "element":
                this.addNewElement(card);
                break;
            case "back":
                this.addNewBack(card);
                break;
			default:
				console.log("unknown card type at cache card builder");
				this.addNewCreature(card);
		}
	}
	
	addNewCreature(card){
		let pool = manaPool.buildPool(3,"&nbsp;",card.cost);
		let cardCost = new cost(card.cost.resources,pool);
		let newCard = new creatureCard(card.id,cardCost,card.name,card.text,card.image,card.attack,card.health);
		parseStates(card,newCard);
        this.setByIID(card.id,newCard);
	}
    
    addNewSpell(card){
        let pool = manaPool.buildPool(3,"&nbsp;",card.cost);
        let cardCost = new cost(card.cost.resources,pool);
        let newCard = new spellCard(card.id,cardCost,card.name,card.text,card.image);
        parseStates(card,newCard);
        this.setByIID(card.id,newCard);
    }
    
    addNewElement(card){
        let newCard = new elementCard(card.id,card.elementType);
        this.setByIID(card.id,newCard);
        parseStates(card,newCard);
    }
    
    addNewBack(card){
        let newCard = new cardBack(card.id);
        this.setByIID(card.id,newCard);
        parseStates(card,newCard);
    }
    
    parseStates(card,newCard){
        if(card.states != null){
            newCard.setStates(card.states);
        }
    }
    
    addNewParsedCard(card){
        this.setByIID(card.IID,card);
    }
    
    getCardList(cards){
        let result = [];
        for(let card of cards){
            result.push(this.getByIID(card.IID));
        }
        return result;
    }
	

}