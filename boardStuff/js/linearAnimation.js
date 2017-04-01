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
	
	setStartDiv(divElem){
		this.setStartRange(divElem.offsetLeft,divElem.offsetTop,divElem.offsetLeft + divElem.offsetWidth, divElem.offsetTop + divElem.offsetHeight);
	}
	
	setEndDiv(divElem){
		console.log(divElem);
		this.setEndRange(divElem.offsetLeft,divElem.offsetTop,divElem.offsetLeft + divElem.offsetWidth, divElem.offsetTop + divElem.offsetHeight);
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