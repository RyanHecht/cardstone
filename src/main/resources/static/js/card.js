

class Card{
	
	constructor(IID){
		this.IID = IID;
		this.states = [];
	}
	
	setDiv(div){
		this.div = div;
	}
	
	addIdAndState(div){
		div.children(".card").attr("id",this.IID);
		div.children(".card").addClass(this.state);
	}
	
	modifyWith(card){
		
	}
	
	getZone(){
		return this.zone;
	}
	
	setZone(zone){
		this.zone = zone;
	}
	
	setStates(states){
		this.states = states;
	}
    
    drawStates(div){
        let taunt = false;
        let canPlay = false;
        let canAttack = false;
        for(let state of this.states){
            switch(state){
                case "taunt":
                    taunt = true;
                    break;
                case "canPlay":
                    canPlay = true;
                    break;
                case "canAttack":
                    canAttack = true;
                    break;
                default:
                    console.log("unknown state", state);
                    break;
            }
        }
        console.log(this.name, taunt, canPlay, canAttack, this.states, this.div);
        if(taunt){
            div.addClass("taunt");
        }
        else{
            div.removeClass("taunt");
        }
        if(canPlay || canAttack){
            console.log("acted", canPlay, canAct);
            div.addClass("canAct");
        }
        else{
            div.removeClass("canAct");
        }
    }
	
}

class creatureCard extends Card {
	constructor(IID,cost, name, text, imagePath, attack,health){
		super(IID);
		this.cost = cost;
		this.name = name;
		this.text = text;
		this.imagePath = imagePath;
		this.health = health;
		this.attack = attack;
		this.IID = IID;
        this.type = "creature";
	}
	modifyWith(info){
		super.modifyWith(info);
		if(info.name != null){
			this.name = info.name;
		}
		if(info.cost != null){
			this.cost = info.cost;
		}
		if(info.attack != null){
			this.attack = info.attack;
		}
		if(info.health != null){
			this.health = info.health;
		}
		if(info.image != null){
			this.imagePath = info.image;
		}
		if(info.text != null){
			this.text = info.text;
		}
		if(info.cost != null){
			let pool = manaPool.buildPool(3,"&nbsp;",info.cost);
			this.cost = new cost(info.cost.resources,pool);
		}
		return this;
	}
	
	drawTiny(div){
		div.html(tinyCardHtml);
        this.drawStates(div);
		div = div.children(".card");
		div.children(".imageArea").children(".cardImage").attr("src", this.imagePath);
		let stats = div.children(".statArea");
		stats.children(".health").html(this.health);
		stats.children(".attack").html(this.attack);
		//div.children(".statArea").css({background: this.cost.getColor()});
		div[0].style.background = this.cost.getColor();
        
	}
	drawSmall(div){
		div.html(smallCardHtml);
        this.drawStates(div);
		div = div.children(".card");
		div.children(".name").text(this.name);
		this.cost.draw(div.children(".cost"));
		div.children(".imageArea").children(".cardImage").attr("src", this.imagePath);
		let stats = div.children(".statArea");
		stats.children(".health").html(this.health);
		stats.children(".attack").html(this.attack);
		div[0].style.background = this.cost.getColor();
		
		//.css({"background" : this.cost.getColorGradient()});
	}
	drawBig(div){
		div.html(bigCardHtml);
		div.children(".text").text(this.text);
		div = div.children(".card");
		div.children(".name").text(this.name);
		this.cost.draw(div.children(".cost"));
		div.children(".imageArea").children(".cardImage").attr("src", this.imagePath);
		let stats = div.children(".statArea");
		stats.children(".health").html(this.health);
		stats.children(".attack").html(this.attack);
		div[0].style.background = this.cost.getColor();
		//div.children(".statArea")[0].style.background = this.cost.getColor();
		div.children(".cardText").text(this.text);
	}
	
	drawSmallAndHiddenBig(div){
		this.drawSmall(div);
		div.append('<div class="bigCardBox"></div>');
		div = div.children('.bigCardBox');
		this.drawBig(div);
		div.hide();
		
	}
	
	drawTinyAndHiddenBig(div){
		this.drawTiny(div);
		div.append('<div class="bigCardBox"></div>');
		div = div.children('.bigCardBox');
		this.drawBig(div);
		div.hide();
	}
	
	drawGivenSpace(div = this.div){
		if(div.height() > 125){
			this.drawForTip();
		}
		else{
			this.drawTinyForTip();
		}
		super.addIdAndState(this.div);
	}
	
	drawForTip(){
		this.drawSmallAndHiddenBig(this.div);

	}
	drawTinyForTip(){
		this.drawTinyAndHiddenBig(this.div);
	}
	
	
	
}

class spellCard extends Card{
	
	constructor(IID, cost, name, text, imagePath){
		super(IID);
		this.cost = cost;
		this.name = name;
		this.text = text;
		this.imagePath = imagePath;
		this.IID = IID;
        this.type ="spell";
	}
	modifyWith(info){
		if(info.name != null){
			this.name = info.name;
		}
		if(info.cost != null){
			this.cost = info.cost;
		}
		if(info.image != null){
			this.imagePath = info.image;
		}
		if(info.text != null){
			this.text = info.text;
		}
		return this;
	}
	
	drawTiny(div){
		div.empty();
		div.html(tinySpellCardHtml);
        this.drawStates(div);
		div = div.children(".card");
		div.children(".imageArea").children(".cardImage").attr("src", this.imagePath);
		this.cost.draw(div.children(".cost"));
		div[0].style.background = this.cost.getColor();
	}
	drawSmall(div){
		div.html(smallSpellCardHtml);
        this.drawStates(div);
		div = div.children(".card");
		div.children(".name").text(this.name);
		this.cost.draw(div.children(".cost"));
		div.children(".imageArea").children(".cardImage").attr("src", this.imagePath);
		div[0].style.background = this.cost.getColor();
	}
	drawBig(div){
		div.html(bigSpellCardHtml);
		div.children(".text").text(this.text);
		div = div.children(".card");
		div.children(".name").text(this.name);
		this.cost.draw(div.children(".cost"));
		div.children(".imageArea").children(".cardImage").attr("src", this.imagePath);
		div[0].style.background = this.cost.getColor();
		div.children(".cardText").text(this.text);
	}
	
	drawSmallAndHiddenBig(div){
		this.drawSmall(div);
		div.append('<div class="bigCardBox"></div>');
		div = div.children('.bigCardBox');
		this.drawBig(div);
		div.hide();
		
	}
	
	drawTinyAndHiddenBig(div){
		this.drawTiny(div);
		div.append('<div class="bigCardBox"></div>');
		div = div.children('.bigCardBox');
		this.drawBig(div);
		div.hide();
	}
	
	drawGivenSpace(){
		let div = this.div;
		if(div.height() > 125){
			this.drawForTip();
		}
		else{
			this.drawTinyForTip();
		}
		super.addIdAndState(this.div);
	}
	
	drawForTip(){
		this.drawSmallAndHiddenBig(this.div);

	}
	drawTinyForTip(){
		this.drawTinyAndHiddenBig(this.div);
	}
}

class cardBack extends Card{
	
	constructor(IID){
		super(IID);
	}
	
	drawGivenSpace(){
		let div = this.div;
		this.drawSmallAndBig(div);
		super.addIdAndState(this.div);
	}
	
	modifyWith(info){
		if(info.TID != null){
			return CardRepo.get(info.TID);
		}
		else{
			if(info.type == null){
				
            }
			else{
				return info.createAlone();
			}
		}
	}
	
	drawSmallAndBig(div){
		div.html(cardBackHtml);
        this.drawStates(div);
		div.append('<div class="bigCardBox"></div>');
		div = div.children('.bigCardBox');
		div.html(cardBackHtml);
		div.addClass("hasToolTip");
		div.hide();
	}
	
	
}

class elementCard extends Card{
	
	constructor(IID, elementType){
		super();
		this.IID = IID;
		this.elementType = elementType;
		switch(elementType){
			case("water"):
				this.color = waterText;
                this.name = "Water";
				break;
			case("fire"):
				this.color = fireText;
                this.name = "Fire"
				break;
			case("air"):
				this.color = airText;
                this.name = "Air"
				break;
			case("earth"):
				this.color = earthText;
                this.name = "Earth"
				break;
			case("balance"):
				this.color = balanceText;
                this.name = "Balance"
				break;
		}
        this.name += " Element"; 
		this.cost = {
            regRes : -1
        }
        this.type="element";
	}
	
	modifyWith(info){
		if(info.TID != null){
			console.log("not possible to modify an element card currently");	
		}
	}
    
    drawBig(div){
        div.html(elementHtml);
		div.children(".card").children(".imageArea").children(".cardImage").attr("src", "images/elements/"+this.elementType+"Big.jpg");
		div.addClass("hasToolTip");
		div.children(".card")[0].style.background = this.color;
    }
	
	drawGivenSpace(){
		this.drawSmallAndBig(this.div);
		super.addIdAndState(this.div);
	}
	
	drawSmallAndBig(div){
		div.html(elementHtml);
        this.drawStates(div);
		div.children(".card").children(".imageArea").children(".cardImage").attr("src", "images/elements/"+this.elementType+"Big.jpg");
		div.children(".card")[0].style.background = this.color;
		div.append('<div class="bigCardBox"></div>');
		div = div.children('.bigCardBox');
		div.html(elementHtml);
		div.children(".card").children(".imageArea").children(".cardImage").attr("src", "images/elements/"+this.elementType+"Big.jpg");
		div.addClass("hasToolTip");
		div.children(".card")[0].style.background = this.color;
		div.hide();
	}
	
}



let elementHtml = '<div class="card elementCard hasTooltip">' + 
				'<div class="cardpart imageArea"><image class="cardImage" src="fail.jpg"></image></div>' + 
				'</div>';

let cardBackHtml = '<div class="card cardBack hasTooltip">' + 
				'<div class="cardpart imageArea"><image class="cardImage" src="images/cardBack.jpg"></image></div>' + 
			'</div>';

let tinyCardHtml = '<div class="card tinyCard creatureCard hasTooltip">' + 
				'<div class="cardpart imageArea"><image class="cardImage" src="fail.jpg"></image></div>' + 
				'<div class="cardpart statArea">'+
				'<div class="stat attack"></div><div class="stat health"></div>' +
				'</div>'+
			'</div>';

let smallCardHtml = '<div class="card  smallCard hasTooltip creatureCard">' + 
				'<div class="cardpart name"></div>' + 
				'<div class="cardpart cost"></div>' + 
				'<div class="cardpart imageArea"><image class="cardImage" src="fail.jpg"></image></div>' + 
				'<div class="cardpart statArea">'+
				'<div class="stat attack"></div><div class="stat health"></div>' +
				'</div>'+
			'</div>';
			
let bigCardHtml = '<div class="card creatureCard bigCard">' + 
				'<div class="cardpart name"></div>' + 
				'<div class="cardpart cost"></div>' + 
				'<div class="cardpart imageArea"><image class="cardImage" src="fail.jpg"></image></div>' + 
				'<div class="cardpart cardText"></div>' + 
				'<div class="cardpart statArea">'+
				'<div class="stat attack"></div><div class="stat health"></div>' +
				'</div>'+
			'</div>';

let tinySpellCardHtml = '<div class="card tinyCard spellCard hasTooltip">' + 
				'<div class="cardpart cost"></div>' + 
				'<div class="cardpart imageArea"><image class="cardImage" src="fail.jpg"></image></div>' + 
			'</div>';

let smallSpellCardHtml = '<div class="card spellCard smallCard hasTooltip">' + 
				'<div class="cardpart name"></div>' + 
				'<div class="cardpart cost"></div>' + 
				'<div class="cardpart imageArea"><image class="cardImage" src="fail.jpg"></image></div>' + 
				'<div class="cardpart statArea">'+
				'</div>'+
			'</div>';
			
let bigSpellCardHtml = '<div class="card spellCard bigCard">' + 
				'<div class="cardpart name"></div>' + 
				'<div class="cardpart cost"></div>' + 
				'<div class="cardpart imageArea"><image class="cardImage" src="fail.jpg"></image></div>' + 
				'<div class="cardpart cardText"></div>' + 
				'<div class="cardpart statArea">'+
				'</div>'+
			'</div>';
	