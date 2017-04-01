class manaZone{
	constructor(div,pool){
		this.div = div;
		this.pool = pool;
	}
	
	draw(){
		this.div.empty();
		this.pool.draw(this.div);
	}
	
	forceRedraw(){
		this.draw();
	}
}

class manaPool{
	
	constructor(drawLimit, seperator){
		this.drawLimit = drawLimit;
		this.seperator = seperator;
		this.water = 0;
		this.fire = 0;
		this.air = 0;
		this.earth = 0;
		this.balance = 0;
	}
	
	getTotalColors(){
		let total = 0;
		if(this.fire > 0){
			total++;
		}
		if(this.air > 0){
			total++;
		}
		if(this.water > 0){
			total++;
		}
		if(this.earth > 0){
			total++;
		}
		if(this.balance > 0){
			total++;
		}
		return total;
	}
	
	getColor(){
		let x = this.getTotalColors();
		if(x == 0){
			return "white";
		}
		else if(x >= 3){
			return "white";
		}
		else if(x == 1){
			if(this.fire > 0){
				return fireText;
			}
			if(this.earth > 0){
				return earthText;
			}
			if(this.water > 0){
				return waterText;
			}
			if(this.air > 0){
				return airText;
			}
			if(this.balance > 0){
				return balanceText;
			}
		}
		else if(x == 2){
			return this.getColorGradient();
		}
	}
	
	getColorGradient(){
		let result = "linear-gradient(to left"
		if(this.fire > 0){
			result = result + ", " + fireText;
		}
		if(this.air > 0){
			result = result + ", " + airText;
		}
		if(this.earth > 0){
			result = result + ", " + earthText;
		}
		if(this.water > 0){
			result = result + ", " + waterText;
		}
		if(this.balance > 0){
			result = result + ", darkgrey";
		}
		result = result + ")"
		return result;
	}
	
	drawElement(div,elementName,elementVal,total){
		div.append('<div class="resDisplay manaRes '+elementName+'Res"></div>');
		let box = div.children('.'+elementName+'Res');
		if(elementVal > this.drawLimit){
			box.append('<img src="images/elements/'+elementName+'.jpg">:');
			box.append(elementVal);
		}
		else{
			for(let x = 0; x < elementVal; x++){
					box.append('<img src="images/elements/'+elementName+'.jpg">')

			}
		}
		if(total > 0){
			div.append(this.seperator);
		}
	}
	
	draw(div){
		let total = this.fire + this.water + this.air + this.balance + this.earth;
		if(this.fire > 0){
			total -= this.fire;
			this.drawElement(div,"fire",this.fire,total);
		}
		if(this.water > 0){
			total -= this.water;
			this.drawElement(div,"water",this.water,total);
		}
		if(this.earth > 0){
			total -= this.earth;
			this.drawElement(div,"earth",this.earth,total);
		}
		if(this.air > 0){
			total -= this.air;
			this.drawElement(div,"air",this.air,total);
		}
		if(this.balance > 0){
			total -= this.fire;
			this.drawElement(div,"balance",this.balance,total);
		}
	}
	
	setFire(fire){
		this.fire = fire;
	}
	
	setWater(water){
		this.water = water;
	}
	
	setAir(air){
		this.air = air;
	}
	
	setEarth(earth){
		this.earth = earth;
	}
	
	setBalance(balance){
		this.balance = balance;
	}
	
}