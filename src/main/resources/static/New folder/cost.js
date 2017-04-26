class cost{

	constructor(regRes, manaPool){
		this.regRes = regRes;
		this.manaPool = manaPool;
	}
	
	draw(div){
		div.empty();
		div.append('<div class="resDisplay regRes"></div>');
		div.children('.regRes').html(this.regRes + "&nbsp");
		this.manaPool.draw(div);
	}
	
	getColor(){
		return this.manaPool.getColor();
	}

}