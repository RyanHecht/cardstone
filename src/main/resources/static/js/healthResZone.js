class healthResZone extends drawableZone{
	constructor(div,health,res,deck){
		super();
		this.healthDiv = div.find('.health');
		this.resDiv = div.find('.regRes');
		this.deckDiv = div.find(".deck");
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
					viewport: $(window),
					adjust: {
						method: 'flipinvert shift'
					}
					
			}
		}
		);
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