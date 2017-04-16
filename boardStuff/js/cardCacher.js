class cardCacher{

	constructor(){
		this.cache = new Map();
	}

	addInfo(info){
		if(!info.modded){
			return;
		}
		if(this.cache.has(info.IID)){
			let toSet = this.cache.get(info.IID);
			this.cache.set(info.IID,toSet.modifyWith(info));
		}
		else{
			this.cache.set(info.IID,info.buildAlone());
		}
	}
	
	getByIID(IID){
		if(cache.has(IID)){
			return cache.get(IID);
		}
	}
	
	

}
}