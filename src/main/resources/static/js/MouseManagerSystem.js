class MouseManagerSystem{

    constructor(){
        this.isClicked = false;
        this.curTargeter = 0;
        this.curTarget = 0;
        this.redraw();
    }
        
    mousedown(id,event){
        this.isClicked = true;
        this.curTargeter = id;
        canvasLine.x1 = this.transformX(event.pageX);
        canvasLine.y1 = this.transformY(event.pageY);
        $(".qtip").addClass("tooltipHidden");
        $("#"+id).parent().addClass("cardBoxHighlightedClick");
        
    }
    
    mousemoved(event){
        canvasLine.x2 = this.transformX(event.pageX);
        canvasLine.y2 = this.transformY(event.pageY);
        if(event.which == 0){
            this.mouseupLight();
        }
    }
    
    redraw(){
        this.tips = $(".qtip");
    }
    
    mouseupLight(){
        if(this.isClicked){
            this.mouseup();
        }
    }
    
    mouseup(event){
       this.isClicked = false;
       this.tips.removeClass("tooltipHidden");
       $('div.qtip:visible').qtip('hide');
       event.stopPropagation();
       $("#"+this.curTargeter).parent().removeClass("cardBoxHighlightedClick");
    }
    
    mouseupCard(id,event){ 
        if(this.isClicked){
            if(this.curTargeter != id){
                server.cardTargeted(this.targeter,id);
            }
        }
        this.mouseup(event);
    }
    
    mouseupDiv(div,event){
        if(this.isClicked){
            server.cardPlayed(this.curTargeted,div);
        }
        this.mouseup(event);
    }
    
    transformX(x){
        return x;
    }
    
    transformY(x){
        return x;
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