class boardInfo{

	constructor(){
		this.features = new Map();
		this.zones = new Map();
	}

	setFeature(name,val){
		this.features.set(name,val);
	}
	
	setZone(name,val){
		this.zones.set(name,val);
	}
	
	
	modifyBoard(board,cache){
		let iter = this.features.entries();
		for(let keys of iter){
			board.changeFeature(keys[0],keys[1]);
		}
		iter = this.zones.entries();
		for(let entry of iter){
			for(let info of entry[1]){
				cache.addInfo(info);
			}
			board.changeZone(entry[0],entry[1]);
		}
	}
	
	
	
	pushZone(collectionInfo,zone,owner){
		if(!collectionInfo.modded){
			return;
		}
		else{
			switch(zone) {
			case ZoneEnum.HAND:
				if(owner == 1){
					this.hand1 = collectionInfo;
				}
				else{
					this.hand2 = collectionInfo;
				}
				break;
			case ZoneEnum.AURA:
				if(owner == 1){
					this.aura1 = collectionInfo;
				}
				else{
					this.aura2 = collectionInfo;
				}
				break;
			case ZoneEnum.CREATURE:
				if(owner == 1){
					this.creature1 = collectionInfo;
				}
				else{
					this.creature2 = collectionInfo;
				}
				break;
			default:
				console.log("unknown zone in board pushcard");
		}
	}

}