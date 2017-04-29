class animation{
	constructor(){
		this.random = false;
	}
	
	create(){
		console.log("Cannot create basic animation type. Use a subclass!");
	}
	
	setColor(color){
		this.ranged = false;
		this.color = color;
		this.colorSet = null;
	}
	
	buildFromOptions(options){
		let colorType = options.colorType;
		this.setColorFromData(colorType,options);
		this.setShape(options.shape);
		this.setRadius(options.radis);
		this.setSpeed(options.speed);
	}
	
	setColorFromData(type,data){
		switch(colorType){
		case ColorEnum.DEFAULT:
			this.setColor(DEFAULT_ANIM_COLOR);
		case ColorEnum.MONO:
			this.setColor(data.color);
		case ColorEnum.RANGED:
			this.setColorRange(data.r1,data.r2,data.g1,data.g2,data.b1,data.b2,data.a);
		}
	}
	
	setColorRange(r1,r2,g1,g2,b1,b2,a){
		this.ranged = true;
		this.r1 = r1;
		this.r2 = r2;
		this.g1 = g1;
		this.g2 = g2;
		this.b1 = b1;
		this.b2 = b2;
		this.a = a;
		this.colorSet = null;
	}
	
	setColorSet(colorSet){
		this.colorSet = colorSet;
	}
	
	setShape(shape){
		if(shape != null){
			this.shape = shape;
		}
	}
	
	setRadius(radius){
		if(radius != null){
			this.radius = radius;
		}
	}

	setCount(count){
		if(count != null){
			this.count = count;
		}
	}
	
	setRandom(random){
		this.random = random;
	}
	
	setSpeed(speed){
		if(speed != null){
			this.speed = speed;
		}
	}
	
	setColorRange(r1,r2,g1,g2,b1,b2,a){
		this.ranged = true;
		this.r1 = r1;
		this.r2 = r2;
		this.g1 = g1;
		this.g2 = g2;
		this.b1 = b1;
		this.b2 = b2;
		this.a = a;
	}
	
	setShape(shape){
		this.shape = shape;
	}
	
	setRadius(radius){
		this.radius = radius;
	}

	setCount(count){
		this.count = count;
	}
	
	setRandom(random){
		this.random = random;
	}
	
	setSpeed(speed){
		this.speed = speed;
	}
}