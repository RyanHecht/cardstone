class card {
	constructor(cost, name, text, ident, imagePath, stats){
		this.cost = cost;
		this.name = name;
		this.text = text;
		this.ident = ident;
		this.imagePath = imagePath;
		this.stats = stats;
	}
	drawTiny(div){
		div.html(tinyCardHtml);
		div = div.children(".card");
		div.children(".imageArea").children(".cardImage").attr("src", this.imagePath);
		div.children(".statArea").text(this.stats);
	}
	
	drawSmall(div){
		div.html(smallCardHtml);
		div = div.children(".card");
		div.children(".name").text(this.name);
		div.children(".cost").text(this.cost);
		div.children(".imageArea").children(".cardImage").attr("src", this.imagePath);
		div.children(".statArea").text(this.stats);
	}
	drawBig(div){
		div.html(bigCardHtml);
		div.children(".text").text(this.text);
		div = div.children(".card");
		div.children(".name").text(this.name);
		div.children(".cost").text(this.cost);
		div.children(".imageArea").children(".cardImage").attr("src", this.imagePath);
		div.children(".statArea").text(this.stats);
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
	
	drawGivenSpace(div){
		//naive solution needs fixing eventually.
		if(div.height() > 125){
			this.drawForTip(div);
		}
		else{
			this.drawTinyForTip(div);
		}
	}
	
	drawForTip(div){
		this.drawSmallAndHiddenBig(div);
	}
	drawTinyForTip(div){
		this.drawTinyAndHiddenBig(div);
	}
	
}


let tinyCardHtml = '<div class="card tinyCard hasTooltip">' + 
				'<div class="cardpart imageArea"><image class="cardImage" src="fail.jpg"></image></div>' + 
			'</div>';

let smallCardHtml = '<div class="card smallCard hasTooltip">' + 
				'<div class="cardpart name"></div>' + 
				'<div class="cardpart cost"></div>' + 
				'<div class="cardpart imageArea"><image class="cardImage" src="fail.jpg"></image></div>' + 
				'<div class="cardpart statArea"></div>'+
			'</div>';
			
let bigCardHtml = '<div class="card bigCard">' + 
				'<div class="cardpart name"></div>' + 
				'<div class="cardpart cost"></div>' + 
				'<div class="cardpart imageArea"><image class="cardImage" src="fail.jpg"></image></div>' + 
				'<div class="cardpart cardText"></div>' + 
				'<div class="cardpart statArea"></div>'+
			'</div>';
	