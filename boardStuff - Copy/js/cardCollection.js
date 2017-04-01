class cardCollection{
	constructor(div,cards){
		this.div = div;
		this.cards = cards;
		this.changed = true;
		//this.prepareForEnlarge();
	}
	
	setCards(cards){
		this.cards = cards;
		this.changed = true;
	}
	
	forceRedraw(){
		this.changed = true;
		this.draw();
	}
	
	pushCard(card){
		this.cards.push(card);
		this.changed = true;
	}
	
	fillDiv(){
		let curDiv = this.div;
		let $this = this;
		let baseWidth = (curDiv.height() * WIDTH_RATIO) + 10;
		let rows = 1;
		while(baseWidth * ($this.cards.length / rows) >= curDiv.width() * .95){
			rows++;
			baseWidth = ((curDiv.height() / rows) * WIDTH_RATIO) + 10;
		}
		let maxInRow = Math.ceil($this.cards.length / rows);
		if(maxInRow == 0){
			console.log("Problem with row sizing in filldiv");
		}
		let total = 0;
		for(let x = 1; x <= rows; x++){
			curDiv.append('<div class="cardRow"></div');
			let curChild = curDiv.children().last();
			while(total < x * maxInRow && total < this.cards.length){
				//space to mekkit mejor
				curChild.append('<div class="cardBox"></div>')
				let cur = curChild.children().last();
				cur.css('height', $this.div.height() / rows + "px");
				$this.cards[total].drawGivenSpace(cur);
				total++;
			}
		}
	}
	
	draw(){
		if(this.changed){
			this.changed = false;
			this.div.empty();
			this.fillDiv();
			this.prepareToolTips();
			this.sizeCards();
		}
	}
	
	sizeCards(){
		this.div.children().children('.cardBox').css({
			'width':function(index, value){
				return $(this).height() * WIDTH_RATIO;
			}
		});
	}

	prepareToolTips(){
		this.div.find('.hasTooltip').each(function() {
			$(this).qtip({
				content: {
					text: $(this).next('div') 
				},
				style: {
					classes: 'qtip-bootstrap',
				},
				position: { 
					viewport: $(window),
					adjust: {
						method: 'flipinvert shift'
					}
					
				}
			});
		});
	}
}