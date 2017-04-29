class cardInfo{
	
	constructor(IID,modded){
		this.IID = IID;
		this.modded = modded;
	}
	
	setTID(TID){
		this.TID = TID;
		this.modded = true;
	}
	
	setState(state){
		this.state = state;
		this.modded = true;
	}
	
	setAttack(attack){
		this.attack = attack;
		this.modded = true;
	}
	
	setHealth(health){
		this.health = health;
		this.modded = true;
	}
	
	setCost(cost){
		this.cost = cost;
		this.modded = true;
	}
	
	setName(name){
		this.name = name;
		this.modded = true;
	}
	
	setType(type){
		this.type = type;
		this.modded = true;
	}
	
	setText(text){
		this.text = text;
		this.modded = true;
	}
	
	buildCreature(){
		return new creatureCard(this.IID,this.cost,this.name,this.text,this.imagePath,this.attack,this.health);
	}
	
	buildSpell(){
		
	}
	
	buildAlone(){
		if(this.type == TypeEnum.CREAUTRE){
			return this.buildCreature();
		}
		else if(this.type == TypeEnum.SPELL){
			return this.buildSpell();
		}
		else if(this.type = TypeEnum.BACK){
			return CardRepo.getBack();
		}
		else if(this.type = TypeEnum.ELEMENT){
			return CardRepo.getElement(this.TID);
		}
		else{
			console.log("unknown type encountered while trying to build:: " + this.type);
		}
	}
	
}