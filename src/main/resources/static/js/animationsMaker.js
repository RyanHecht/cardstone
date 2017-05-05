function killAnimBox(){
    let box = $(".animBox");
    box.empty();
    box.hide();
    $(".chat").show();
}

function getDrawnCardAnimation(isMe){
    let anim;
    anim = new cardDrawnAnimation();
    anim.setTarget(isMe);
    
    return anim;
}

class animationsMaker{	

    static playCardAnimation(card){
        cardCache.repairCard(card);
        let box = $(".animBox");
        box.empty();
        let cardFromCache = cardCache.getByIID(card.id);
        console.log(cardFromCache);
        box.append("<div class='cardBox'></div>");
        console.log(box);
        console.log(box.children());
        cardFromCache.drawGivenSpace(box.children(".cardBox"));
        box.show();
        $(".chat").hide();
        $("#" + card.id).first().attr("id",-180);
        window.setTimeout(killAnimBox,3000);
        box.click(killAnimBox);
    }
    


	static getAttackAnimation(attacker,defender){
		let anim = new linearAnimation();
		let attackerCard = cardCache.getByIID(attacker);
		anim.setColorSet(attackerCard.cost.manaPool.getColors());
		anim.setShape("circle");
		anim.setRadius(1);
		anim.setCount(150);
		anim.setSpeed(.5);
		anim.setRandom(true);
		anim.setStartDivById(attacker);
		anim.setEndDivById(defender);
		return anim;
	}
	
	static getDamagedAnimation(damaged){
		let anim = new singleRadial();
		anim.setColor("red");
		anim.setShape("circle");
		anim.setRadius(50);
		anim.setSpeed(.3);
		anim.setCenterDivById(damaged);
		anim.setCount(3);
		return anim;
	}
    
    static getDeadAnimation(damaged){
        let anim = new singleRadial();
		anim.setColor("black");
		anim.setShape("circle");
		anim.setRadius(50);
		anim.setSpeed(.3);
		anim.setCenterDivById(damaged);
		anim.setCount(3);
		return anim;
    }
    
    static getPlayerDamagedAnimation(isMe){
        let anim = new singleRadial();
		anim.setColor("red");
		anim.setShape("circle");
		anim.setRadius(50);
		anim.setSpeed(.3);
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
		anim.setSpeed(.5);
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

