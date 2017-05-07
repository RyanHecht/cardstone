const CARD_DRAW_ANIM_HEIGHT_OFFSET = 80;
const CARD_DRAW_ANIM_HEIGHT = 140;

class cardDrawnAnimation extends animation{
    
    constructor(){
		super();
        let off = $("#lineRight").offset();
        let offWidth = $("#lineRight")[0].offsetWidth;
        this.setStartLoc(off.left offWidth,off.top);
        this.speed = .6;
	}
    
    create(){
        let drawables = [];
        let drawable = new movingDrawable("card",this.startX,this.startY,this.goalX,this.goalY,"red",this.speed,5);
        drawables.push(drawable);
        return drawables;
    }
    setStartLoc(startX, startY){
        this.startX = startX;
        this.startY = startY;
    }
    setTarget(isMe){
        let wind = $(window);
        this.goalX = wind.width() / 2;
        if(isMe){
            this.goalY = wind.height() - CARD_DRAW_ANIM_HEIGHT_OFFSET;
        }
        else{
            this.goalY = 0;
        }
    }
}