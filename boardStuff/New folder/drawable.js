class drawable{

	constructor(shape,speed,color,radius){
		this.radius = radius;
		this.shape = shape;
		this.color = color;
		this.speed = speed;
	}

	draw(ctx){
		ctx.beginPath(0,0);
		ctx.strokeStyle = this.color ;
		if(this.shape == "circle"){
			ctx.ellipse(this.x,this.y,this.radius,this.radius,0,0,10);
		}
		ctx.closePath();
		ctx.stroke();
	}

}