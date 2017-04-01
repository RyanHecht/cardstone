var ZoneEnum = {
  HAND: 1,
  AURA: 2,
  CREATURE: 3
};

class board{	
	constructor(hand1, hand2, aura1, aura2, creature1, creature2, p1Health, p2Health, p1RegRes, p2RegRes,p1Mana,p2Mana,deck1,deck2){
		this.hand1 = new cardCollection($('#hand1'),hand1,$('#onTop1'));
		this.hand2 = new cardCollection($('#hand2'),hand2,$('#onTop2'));
		this.aura1 = new cardCollection($('#aura1'),aura1,$('#onTop1'));
		this.aura2 = new cardCollection($('#aura2'),aura2,$('#onTop2'));
		this.creature1 = new cardCollection($('#creature1'),creature1,$('#onTop1'));
		this.creature2 = new cardCollection($('#creature2'),creature2,$('#onTop2'));
		this.p2Health = p2Health;
		this.p1Health = p1Health;
		this.p1RegRes = p1RegRes;
		this.p2RegRes = p2RegRes;
		this.healthRes1 = new healthResZone($('#baseRes1'),p1Health,p1RegRes,deck1);
		this.healthRes2 = new healthResZone($('#baseRes2'),p2Health,p2RegRes,deck2);
		this.p1Mana = new manaZone($('#mana1'),p1Mana);
		this.p2Mana = new manaZone($('#mana2'),p2Mana);
		this.allZones = [this.hand1,this.hand2,this.aura1,this.aura2,this.creature1,this.creature2, this.healthRes1, this.healthRes2, this.p1Mana, this.p2Mana];
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