class radialAnimation extends animation{

	constructor(){
		super();
		this.imploding = true;
	}
	
	setCenterDivById(divID){
		let $divElem = $("#"+divID);
        this.setCenterDiv($divElem);
	}
    
    setCenterDiv($divElem){
        let rect = $divElem[0].getBoundingClientRect();
		let left = rect.left;
		let top = rect.top;
        let width = rect.width;
        let height = rect.height;
		let centerX = left + (width / 2);
		let centerY = top + (height / 2);
		this.setCenter(centerX,centerY);
    }
	
	buildFromOptions(options){
		super.buildFromOptions(options);
		this.setCenterDiv(options.start);
		this.setImploding(options.imploding);
	}
	
	setCenter(x,y){
		this.x = x;
		this.y = y;
	}
	
	setImploding(imploding){
		this.imploding = imploding;
	}
	
}

class singleRadial extends radialAnimation{
	
	create(){
		let drawables = [];
		let color;
		if(this.ranged){
			let r = Math.floor((Math.random() * (this.r2 - this.r1))) + this.r1;
			let g = Math.floor((Math.random() * (this.g2 - this.g1))) + this.g1;
			let b = Math.floor((Math.random() * (this.b2 - this.b1))) + this.b1;
			color = "rgba(" + r + "," + g + "," + b + "," + this.a + ")";
		}
		else{
			color = this.color;
		}
		if(this.imploding){
			for(let x = 0; x < this.count; x ++){
				drawables.push(new collapsingSingleRadialDrawable(this.x, this.y, color,this.shape,this.speed,this.radius - x));
			}
		}
		else{
			for(let x = 0; x < this.count; x++){
				drawables.push(new explodingSingleRadialDrawable(this.x, this.y, color,this.shape,this.speed,this.radius + x));
			}
		}
		return drawables;
	}
	
}

class singleRadialDrawable extends drawable{
	constructor(x,y,color,shape,speed,radius){
		super(shape,speed,color,radius);
		this.x = x;
		this.y = y;
	}
}

class explodingSingleRadialDrawable extends singleRadialDrawable{
	constructor(x,y,color,shape,speed,radius){
		super(x,y,color,shape,speed,radius);
		this.maxRadius = this.radius;
		this.radius = .01;
	}
	update(drawable,delta){
		drawable.radius += (drawable.speed * delta);
		if(drawable.radius >= drawable.maxRadius){
			return true;
		}
		return false;
	}
}

class collapsingSingleRadialDrawable extends singleRadialDrawable{
	update(drawable,delta){
		drawable.radius -= (drawable.speed * delta);
		if(drawable.radius <= 0){
			return true;
		}
		return false;
	}
}

