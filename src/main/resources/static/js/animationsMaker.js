function killAnimBox(){
    let box = $(".animBox");
    box.empty();
    box.hide();
    //$(".chat").show();
}

function getDrawnCardAnimation(isMe){
    let anim;
    anim = new cardDrawnAnimation();
    anim.setTarget(isMe);
    return anim;
}

class animBoxTimer{
    constructor(time){
        this.timeLeft = time;
    }
    
    update(a,delta){
        this.timeLeft -= delta;
        console.log(this.timeLeft);
        if(this.timeLeft <= 0){
            killAnimBox();
            console.log("tried to kill box");
            return true;
        }
        return false;
    }
    
    create(){
        let list = [];
        list.push(this);
        return list;
    }
    
    draw(){
        
    }
}

class animationsMaker{	

    static playCardAnimation(card){
        cardCache.repairCard(card);
        let box = $(".animBox");
        box.empty();
        let cardFromCache = cardCache.getByIID(card.id);
        playedQueue.add(cardFromCache);
        box.append("<div class='cardBox'></div>");
        cardFromCache.drawGivenSpace(box.children(".cardBox"));
        box.show();
        //$(".chat").hide();
        $("#" + card.id).first().attr("id",-180);
        let abt = new animBoxTimer(1500);
        return abt;
    }
    


	static getAttackAnimation(attacker,defender){
		let anim = new linearAnimation();
		let attackerCard = cardCache.getByIID(attacker);
		anim.setColorSet(attackerCard.cost.manaPool.getColors());
		anim.setShape("circle");
		anim.setRadius(1);
		anim.setCount(150);
		anim.setSpeed(.9);
		anim.setRandom(true);
		anim.setStartDivById(attacker);
		anim.setEndDivById(defender);
		return anim;
	}
	
	static getDamagedAnimation(damaged){
		let anim = new singleRadial();
		anim.setColor("red");
		anim.setShape("circle");
		anim.setRadius(150);
		anim.setSpeed(.9);
		anim.setCenterDivById(damaged);
		anim.setCount(3);
		return anim;
	}
    
    static getDeadAnimation(damaged){
        let anim = new singleRadial();
		anim.setColor("black");
		anim.setShape("circle");
		anim.setRadius(150);
		anim.setSpeed(.5);
		anim.setCenterDivById(damaged);
		anim.setCount(3);
		return anim;
    }
    
    static getPlayerDamagedAnimation(isMe){
        let anim = new singleRadial();
		anim.setColor("red");
		anim.setShape("circle");
		anim.setRadius(150);
		anim.setSpeed(.9);
        if(isMe){
            anim.setCenterDiv($(".overlayHUD1"));
        }
        else{
            anim.setCenterDiv($(".overlayHUD2"));
        }
		anim.setCount(3);
		return anim;
    }
    
    static getPlayerAttackedAnimation(attacker,isMe){
        let anim = new linearAnimation();
		let attackerCard = cardCache.getByIID(attacker);
		anim.setColorSet(attackerCard.cost.manaPool.getColors());
		anim.setShape("circle");
		anim.setRadius(1);
		anim.setCount(150);
		anim.setSpeed(.9);
		anim.setRandom(true);
		anim.setStartDivById(attacker);
		if(isMe){
            anim.setEndDiv($(".overlayHUD1"));
        }
        else{
            anim.setEndDiv($(".overlayHUD2"));
        }
		return anim;
    }
}

