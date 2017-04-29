class MouseManagerSystem{

    constructor(){
        this.isClicked = false;
        this.curTargeter = 0;
        this.curTarget = 0;
    }
        
    mousedown(id,event){
        this.isClicked = true;
        this.curTargeter = id;
        canvasLine.x1 = this.transformX(event.pageX);
        canvasLine.y1 = this.transformY(event.pageY);
        $(".qtip").addClass("tooltipHidden");
    }
    
    mousemoved(event){
        canvasLine.x2 = this.transformX(event.pageX);
        canvasLine.y2 = this.transformY(event.pageY);
    }
    
    mouseup(event){
       this.isClicked = false;
       $(".qtip").removeClass("tooltipHidden");
       $('div.qtip:visible').qtip('hide');
       console.log("mouseup");
       event.stopPropagation();
    }
    
    mouseupCard(id,event){
        if(this.isClicked){
            if(this.targeter != id){
                server.cardTargeted(this.targeter,id);
            }
        }
        this.mouseup(event);
    }
    
    transformX(x){
        return x * .76832;
    }
    
    transformY(x){
        return x * 1.2;
    }

}

class DrawnLine{
    constructor(x1,y1,x2,y2){
        this.x1 = x1;
        this.x2 = x2;
        this.y1 = y1;
        this.y2 = y2;
    }
    
    draw(ctx){
        ctx.beginPath(0,0);
        ctx.moveTo(this.x1,this.y1);
        ctx.lineTo(this.x2,this.y2);
        ctx.stroke();
    }
}