class card {
	constructor(cost, name, text, ident, imagePath, attack,health,IID){
		this.cost = cost;
		this.name = name;
		this.text = text;
		this.ident = ident;
		this.imagePath = imagePath;
		this.health = health;
		this.attack = attack;
		this.IID = IID;
	}
	
	drawTiny(div){
		div.html(tinyCardHtml);
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
	
	drawGivenSpace(){
		let div = this.div;
		if(div.height() > 125){
			this.drawForTip();
		}
		else{
			
			this.drawTinyForTip();
		}
	}
	
	drawForTip(){
		this.drawSmallAndHiddenBig(this.div);

	}
	drawTinyForTip(){
		this.drawTinyAndHiddenBig(this.div);
	}
	
	setDiv(div){
		this.div = div;
	}
	
}

class cardBack extends card{

	
	drawGivenSpace(){
		let div = this.div;
		this.drawSmallAndBig(div);
	}
	drawSmallAndBig(div){
		div.html(cardBackHtml);
		div.append('<div class="bigCardBox"></div>');
		div = div.children('.bigCardBox');
		div.html(cardBackHtml);
		div.addClass("hasToolTip");
		div.hide();
	}
	
	
}

class elementCard extends card{
	
	constructor(IID, elementType){
		super();
		this.IID = IID;
		this.elementType = elementType;
		switch(elementType){
			case("water"):
				this.color = waterText;
				break;
			case("fire"):
				this.color = fireText;
				break;
			case("air"):
				this.color = airText;
				break;
			case("earth"):
				this.color = earthText;
				break;
			case("balance"):
				this.color = balanceText;
				break;
		}
		
	}
	
	drawGivenSpace(){
		this.drawSmallAndBig(this.div);
	}
	
	drawSmallAndBig(div){
		div.html(elementHtml);
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

let tinyCardHtml = '<div class="card tinyCard hasTooltip">' + 
				'<div class="cardpart imageArea"><image class="cardImage" src="fail.jpg"></image></div>' + 
				'<div class="cardpart statArea">'+
				'<div class="stat attack"></div><div class="stat health"></div>' +
				'</div>'+
			'</div>';

let smallCardHtml = '<div class="card smallCard hasTooltip">' + 
				'<div class="cardpart name"></div>' + 
				'<div class="cardpart cost"></div>' + 
				'<div class="cardpart imageArea"><image class="cardImage" src="fail.jpg"></image></div>' + 
				'<div class="cardpart statArea">'+
				'<div class="stat attack"></div><div class="stat health"></div>' +
				'</div>'+
			'</div>';
			
let bigCardHtml = '<div class="card bigCard">' + 
				'<div class="cardpart name"></div>' + 
				'<div class="cardpart cost"></div>' + 
				'<div class="cardpart imageArea"><image class="cardImage" src="fail.jpg"></image></div>' + 
				'<div class="cardpart cardText"></div>' + 
				'<div class="cardpart statArea">'+
				'<div class="stat attack"></div><div class="stat health"></div>' +
				'</div>'+
			'</div>';
	