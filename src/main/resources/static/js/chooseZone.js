class chooseZone extends drawableZone{
	
	constructor(div,cards){
		super();
		this.div = div;
		this.cards = cardCache.getCardList(cards);
		this.changed = true;
	}
	
	forceRedraw(){
		this.changed = true;
		this.draw();
	}
	
	forceRedrawLater(wait){
		this.changed = true;
		let $this = this;
		window.setTimeout(function(){$this.draw()}, wait);
	}
	
	fillDiv(div){
		let curDiv = div;
		let $this = this;
		let baseWidth = (curDiv.height() * WIDTH_RATIO) + 15;
		let rows = 1;
		while(baseWidth * ($this.cards.length / rows) >= curDiv.width() * .85){
			rows++;
			baseWidth = ((curDiv.height() / rows) * WIDTH_RATIO) + 6;
		}
		let maxInRow = Math.ceil($this.cards.length / rows);
		if(maxInRow == 0){
			console.log("Problem with row sizing in filldiv");
		}
		let total = 0;
		for(let x = 1; x <= rows; x++){
			curDiv.append('<div class="cardRow"></div');
			let curChild = curDiv.children().last();
			curChild.css('height', (curDiv.height() / rows) + "px");
			while(total < x * maxInRow && total < this.cards.length){
				//space to mekkit mejor
				curChild.append('<div class="cardBox"></div>');
				let cur = curChild.children().last();
				cur.css('height', (curDiv.height() / rows) + "px");
				$this.cards[total].setDiv(cur);
				$this.cards[total].drawGivenSpace(cur);
				total++;
			}
		}
	}
	
	draw(){
		this.drawInDiv(this.div);
	}
	
	forceDrawInDiv(div){
		this.changed = true;
		this.drawInDiv(div);
	}
	
	drawInDiv(div){
		if(this.changed){
			this.changed = false;
			div.empty();
			this.fillDiv(div);
			this.prepareToolTips(div);
			this.sizeCards(div);
			this.prepareChoosing(div);
		}
	}
	
	prepareChoosing(div){
		div.find('.card').click(function(){
			server.sendChosen($(this).attr('id'));
            $("#chooseOneAsk").modal('hide');
		})
	}
	
	sizeCards(div){
		div.children().children('.cardBox').css({
			'width':function(index, value){
				return $(this).height() * WIDTH_RATIO;
			}
		});
	}

	prepareToolTips(div){
		let maxHeight = $(document).height();
		let maxWidth = $(document).width();
		let height;
		if(maxHeight > 400){
			height = 350;
		}
		else{
			height = maxHeight * .8;
			$(".bigCardBox").css({
				fontSize : "10px",
				lineHeight : "90%"
			});
		}
		let width = height * WIDTH_RATIO;
		while(width > maxWidth){
			height -= 10;
			width = height * WIDTH_RATIO;
		}
		div.find('.hasTooltip').each(function() {
			$(this).qtip({
				content: {
					text: $(this).next('div') 
				},
				style: {
					height: height,
					width: width,
					classes: 'qtip-bootstrap no-mouse',
				},
				position: { 
					viewport: $(window),
					adjust: {
						method: 'flipinvert shift'
					},
                    
                    target: 'mouse'
					
				}
			});
		});
	}
}
	
	