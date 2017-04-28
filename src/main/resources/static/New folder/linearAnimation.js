class linearAnimation extends animation{
	
	create(){
		let drawables = [];
		if(!this.random){
			let sdX = ((this.startX2 - this.startX1) / this.count);
			let sdY = ((this.startY2 - this.startY1 ) / this.count);
			let edX = ((this.endX2 - this.endX1) / this.count);
			let edY = ((this.endY2 - this.endY1) / this.count);
			for(let i = 0; i < this.count; i++){
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
				drawables.push(new movingDrawable(this.shape,this.startX1 + (sdX * i),this.startY1 + (sdY * i),this.endX1 + (edX * i), this.endY1 + (edY * i), color, this.speed, this.radius));
			}
		}
		else{
			for(let i = 0; i < this.count; i++){
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
				let x1 = ((Math.random() * (this.startX2 - this.startX1)) + this.startX1);
				let y1 = ((Math.random() * (this.startY2 - this.startY1)) + this.startY1);
				let x2 = ((Math.random() * (this.endX2 - this.endX1)) + this.endX1);
				let y2 = ((Math.random() * (this.endY2 - this.endY1)) + this.endY1);
				drawables.push(new movingDrawable(this.shape,x1,y1,x2,y2,color,this.speed,this.radius));
			}
		}
		return drawables;
	}
	
	buildFromOptions(options){
		super.buildFromOptions(options);
		this.setStartDivById(options.start);
		this.setEndDivById(options.end);
		this.count = options.count;
	}
	
	setStartDivById(id){
		this.setStartDiv($("#" + id)[0]);
	}
	
	setEndDivById(id){
		this.setEndDiv($("#" + id)[0]);
	}
	
	setStartDiv(divElem){
		let left = divElem.offsetLeft;
		let top = divElem.offsetTop;
		let width = divElem.offsetWidth;
		let height = divElem.offsetHeight;
		while(divElem.offsetParent != null){
			divElem = divElem.offsetParent;
			left += divElem.offsetLeft;
			top += divElem.offsetTop;
		}
		this.setStartRange(left,top,left + width, top + height);
	}
	
	setEndDiv(divElem){
		let left = divElem.offsetLeft;
		let top = divElem.offsetTop;
		let width = divElem.offsetWidth;
		let height = divElem.offsetHeight;
		while(divElem.offsetParent != null){
			divElem = divElem.offsetParent;
			left += divElem.offsetLeft;
			top += divElem.offsetTop;
		}
		this.setEndRange(left,top,left + width, top + height);
	}
	
	setStartRange(x1,y1,x2,y2){
		this.startX1 = x1;
		this.startX2 = x2;
		this.startY1 = y1;
		this.startY2 = y2;
	}
	
	setEndRange(x1,y1,x2,y2){
		this.endX1 = x1;
		this.endX2 = x2;
		this.endY1 = y1;
		this.endY2 = y2;
		console.log(x1,x2,y1,y2);
	}
	
	
}