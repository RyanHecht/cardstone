class healthResZone extends drawableZone{
	constructor(div,health,res,deck){
		super();
		this.healthDiv = div.children().children().children().children().children('.health');
		this.resDiv = div.children().children().children().children().children('.regRes');
		this.deckDiv = div.children().children().children().children(".deckTd");
		this.health = health;
		this.res = res;
		this.deck = deck;
	}
	
	draw(){
		this.healthDiv.text(this.health);
		this.resDiv.text(this.res);
		let $this = this;
		this.deckDiv.qtip(
		{
			content: {
				text: $this.deck + " cards left",
			},
			style: {
				classes: 'qtip-bootstrap ',
			},
			position: { 
				my: 'top right',
				at: 'bottom left',
				target: this,
			}
		}
		);
		console.log(this.deckDiv,this.healthDiv,this.resDiv);
	}
	
	forceRedraw(){
		this.draw();
	}
	
	updateHealth(health){
		this.health = health;
	}
	
	updateRes(res){
		this.res = res;
	}
	
	update(health, res){
		this.health = health;
		this.res = res;
	}
}