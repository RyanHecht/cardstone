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
		console.log(zone);
		if(!zone.changed){
			return;
		}
		for(let card of zone.cards){
			this.repairCard(card);
		}
	}
	
	repairCard(card){
		console.log(card);
		if(!card.changed){
			return;
		}
		else if(this.cache.has(card.id)){
			this.modifyCard(card);
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
			default:
				console.log("unknown card type at cache card builder");
				this.addNewCreature(card);
		}
	}
	
	addNewCreature(card){
		console.log(card);
		let pool = manaPool.buildPool(3,"&nbsp;",card.cost);
		let cardCost = new cost(card.cost.resources,pool);
		let newCard = new creatureCard(card.id,cardCost,card.name,card.text,card.image,card.attack,card.health);
		console.log(newCard);
		this.setByIID(card.id,newCard);
	}
	
	

}