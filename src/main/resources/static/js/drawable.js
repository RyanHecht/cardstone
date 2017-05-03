const CARD_DRAW_SHAPE_BOX = $("#cardBeingDrawn");
class drawable{

	constructor(shape,speed,color = "red",radius = 5){
		this.radius = radius;
		this.shape = shape;
		this.color = color;
		this.speed = speed;
        this.toggled = false;
	}

	draw(ctx){
		if(this.shape == "circle"){
            ctx.beginPath(0,0);
            ctx.strokeStyle = this.color;
			ctx.ellipse(this.x,this.y,this.radius,this.radius,0,0,10);
            ctx.lineWidth = 3;
            ctx.shadowBlur = 10;
            ctx.shadowColor = this.color;
            ctx.closePath();
            ctx.stroke();
		}
        else if(this.shape ="card"){
            if(!this.toggled){
                this.toggled = true;
                console.log("shown");
                CARD_DRAW_SHAPE_BOX.show();
            }
            CARD_DRAW_SHAPE_BOX.css("top",this.y);
            CARD_DRAW_SHAPE_BOX.css("left",this.x);
        }
	}
}