class cardCollectionInfo{

	constructor(zoneID,modded){
		this.zoneID = zoneID;
		this.modded = modded;
		this.infos = [];
	}
	
	addCardInfo(info){
		this.infos.push(info);
	}
	
	modifyToCache(cache){
		for(let x = 0; x < this.infos.length; x++){
			cache.addInfo(infos[x]);
		}
	}

	buildCollection(div,expandInto,cache){
		let cards = [];
		for(let x = 0; x < this.infos.length; x++){
			cards.push(cache.get(infos.IID));
		}
		return new Collection(div,cards,expandInto);
	}
	
}