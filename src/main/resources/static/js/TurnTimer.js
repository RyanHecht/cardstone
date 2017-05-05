const DEFAULT_WIDTH = 42;
class TurnTimer{

    constructor(isTurn,maxTime){
        this.willBeTurn = false;
        this.maxTime = maxTime * 10000;
        this.lineRight = $(".lineRight");
        this.lineLeft = $(".lineLeft");
        this.startTurn(isTurn);
        this.updateThread(this);
    }
    
    startTurn(isTurn){
        this.isTurn = isTurn;
        this.timeLeft = this.maxTime;
        this.needsNewTurn = false;
        if(isTurn){
            $(".centerClockRect").addClass("endTurnPng");
            $(".centerClockRect").removeClass("enemyTurnPng");
        }
        else{
            $(".centerClockRect").removeClass("endTurnPng");
            $(".centerClockRect").addClass("enemyTurnPng");
            
        }
        this.lineLeft.show();
    }
    
    updateThread($this){
        $this.timeLeft -= UPDATE_RATE;
        $this.drawLines($this.timeLeft * 100 / $this.maxTime,$this);
        if($this.timeLeft <= 0){
            if($this.isTurn){
                if(!spectator){
                    server.endTurn();
                }
            }
            return;
        }
        window.setTimeout(function(){
            $this.updateThread($this);
        },UPDATE_RATE);
    }
    
    drawLines(portion,$this){
        if(portion > 50){
            $this.lineRight.css("width",DEFAULT_WIDTH + "%");
            $this.lineLeft.css("width",(DEFAULT_WIDTH * (portion - 50)/50) + "%");
        }
        else{
            $this.lineLeft.css("width",0 + "%");
            $this.lineRight.css("width",(DEFAULT_WIDTH * (portion)/50) + "%");
            $this.lineLeft.hide();
        }
    }

}