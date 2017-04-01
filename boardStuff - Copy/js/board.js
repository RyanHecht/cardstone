var ZoneEnum = {
  HAND: 1,
  AURA: 2,
  CREATURE: 3
};

class board{	
	constructor(hand1, hand2, aura1, aura2, creature1, creature2){
		this.hand1 = new cardCollection($('#hand1'),hand1);
		this.hand2 = new cardCollection($('#hand2'),hand2);
		this.aura1 = new cardCollection($('#aura1'),aura1);
		this.aura2 = new cardCollection($('#aura2'),aura2);
		this.creature1 = new cardCollection($('#creature1'),creature1);
		this.creature2 = new cardCollection($('#creature2'),creature2);
		this.allZones = [this.hand1,this.hand2,this.aura1,this.aura2,this.creature1,this.creature2];
	}

	draw(){
		for(let x = 0; x < this.allZones.length; x++){
			this.allZones[x].draw();
		}
	}
	
	forceRedraw(){
		for(let x = 0; x < this.allZones.length; x++){
			this.allZones[x].forceRedraw();
		}
	}
	
	pushCard(card, zone, owner){
		switch(zone) {
			case ZoneEnum.HAND:
				if(owner == 1){
					this.hand1.pushCard(card);
				}
				else{
					this.hand2.pushCard(card);
				}
				break;
			case ZoneEnum.AURA:
				if(owner == 1){
					this.aura1.pushCard(card);
				}
				else{
					this.aura2.pushCard(card);
				}
				break;
			case ZoneEnum.CREATURE:
				if(owner == 1){
					this.creature1.pushCard(card);
				}
				else{
					this.creature2.pushCard(card);
				}
				break;
			default:
				console.log("unknown zone in board pushcard");
		}
	}
	
}