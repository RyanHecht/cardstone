class MouseManagerSystem{

    constructor(){
        this.isClicked = false;
        this.curTargeter = 0;
        this.curTarget = 0;
    }
        
    mousedown(id,event){
        this.isClicked = true;
        this.curTargeter = id;
        canvasLine.x1 = event.pageX;
        canvasLine.y1 = event.pageY;
    }
    
    mousemoved(event){
        if(this.isClicked){
            canvasLine.x2 = event.pageX;
            canvasLine.y2 = event.pageY;
        }
    }

}

class canvasLine{
    constructor(x1,y1,x2,y2){
        this.x1 = x1;
        this.x2 = x2;
        this.y1 = y1;
        this.y2 = y2;
    }
    
    draw(ctx){
        ctx.beginPath(0,0);
        ctx.moveTo(x1,y1);
        ctx.lineTo(x2,y2);
        ctx.stroke();
    }
}