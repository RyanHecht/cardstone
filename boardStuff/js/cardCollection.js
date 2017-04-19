class cardCollection extends drawableZone{
	
	
	constructor(div,cards,expandInto){
		super();
		this.div = div;
		this.cards = cards;
		this.changed = true;
		this.prepareForExpand();
		this.expandInto = expandInto;
		this.zone = div.attr("id");
	}
	
	prepareForExpand(){
		let $this = this;
		this.div.children(".expandButton").click(function(){
			if(!expandedInUse){
				expandedInUse = true;
				$this.expand();
			}
		});
	}
	
	setZones(){
		for(let card of this.cards){
			card.setZone(this.zone);
		}
	}
	
	expand(){
		let $this = this;
		$this.expandInto.toggle();
		$this.forceDrawInDiv($this.expandInto);
		$this.expandInto.click(function(){
			$this.expandInto.empty();
			expandedInUse = false;
			$this.expandInto.hide();
			$('div.qtip:visible').qtip('hide');
            $this.forceRedraw();
		});
	}
	
	setCards(cards){
		this.cards = cards;
		this.changed = true;
	}
	
	setCardsFromCache(cardIDs, cache){
		let cards = [];
		for(let id of cardIDs){
			cards.push(cache.getByIID(id));
		}
		this.cards = cards;
	}
	
	forceRedraw(){
		this.changed = true;
		this.draw();
	}
	
	pushCard(card){
		this.cards.push(card);
		this.changed = true;
	}
	
	fillDiv(div){
		let curDiv = div;
		let $this = this;
		if($this.cards.length < 1){
			return;
		}
		else{
			console.log(this.cards);
		}
		let baseWidth = (curDiv.height() * WIDTH_RATIO) + 6;
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
			this.div.append('<div class="expandButton"></div>');
			this.prepareForExpand();
			this.prepareDraggables();
			this.prepareDroppables();
		}
	}
	
	prepareDraggables(){
		this.div.find(".card").draggable({ 
			revert: false, 
			helper: function(){
				return "<div class='targetCursor'></div>";
			},
			cursorAt: { bottom: 25, right: 25}
		});
	}
	
	prepareDroppables(){
		this.div.find(".card").droppable({
			drop: function( event, ui ) {
				$( this )
				  .addClass( "cardSelected" );
				  server.cardTargeted(ui.draggable,$(this));
			},
			greedy:true
		})
	}
	
	prepareDroppableZone(){
		this.div.droppable({
			drop: function( event, ui ) {
				  server.cardPlayed(ui.draggable,$(this));
			}
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