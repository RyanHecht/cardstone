const HAND_1_DIV = $(".overlayHand1");
const HAND_2_DIV = $(".overlayHand2");
const AURA_1_DIV = $(".aura1");
const AURA_2_DIV = $(".aura2");
const CREATURE_1_DIV = $(".creature1");
const CREATURE_2_DIV = $(".creature2");
const BASE_1_DIV = $(".overlayHUD1");
const BASE_2_DIV = $(".overlayHUD2");
const MANA_1_DIV = $("#mana1");
const MANA_2_DIV = $("#mana2");
const EXPAND_1_DIV = $("#onTop1");
const EXPAND_2_DIV = $("#onTop2");
const HUD_1_DIV = $(".overlayHUD1");
const HUD_2_DIV = $(".overlayHUD2");
const DEVOTION_1_DIV = $("#devotionBox1");
const DEVOTION_2_DIV = $("#devotionBox2");




class board{	
	constructor(hand1, hand2, aura1, aura2, creature1, creature2, p1Health, p2Health, p1RegRes, p2RegRes,p1Mana,p2Mana,deck1,deck2){
		this.allZones = new Map();
		this.allZones.set("hand1",new cardCollection(HAND_1_DIV,hand1,$(EXPAND_1_DIV),false));
        if(isReplay){
            this.allZones.set("hand2",new cardCollection(HAND_2_DIV,hand2,$(EXPAND_2_DIV),false));
		}
        else{
            this.allZones.set("hand2",new cardCollection(HAND_2_DIV,hand2,$(EXPAND_2_DIV),true));
        }
        this.allZones.set("aura1",new cardCollection(AURA_1_DIV,aura1,$(EXPAND_1_DIV),false));
		this.allZones.set("aura2",new cardCollection(AURA_2_DIV,aura2,$(EXPAND_2_DIV),false));
		this.allZones.set("creature1", new cardCollection(CREATURE_1_DIV,creature1,$(EXPAND_1_DIV),false));
		this.allZones.set("creature2", new cardCollection(CREATURE_2_DIV,creature2,$(EXPAND_2_DIV),false,false));
		this.features = new Map();
		this.features.set("p2Health",p2Health);
		this.features.set("p1Health",p1Health);
		this.features.set("p2Deck",deck2);
		this.features.set("p1Deck",deck1);
		this.features.set("p2Mana",p2Mana);
		this.features.set("p1Mana",p1Mana);
		this.features.set("p1RegRes",p1RegRes);
		this.features.set("p2RegRes",p2RegRes);
        const devObj = {
            type:"NO_DEVOTION",
            level:0
        }
        this.features.set("devotion1",devObj);
        this.features.set("devotion2",devObj);
		this.buildResZones();
		this.setZones();
        this.isFlipped = false;
	}
    
    flipNow(){
        this.flipZones();
        this.isFlipped = true;
    }
    
    flipTry(){
        if(!this.isFlipped){
            this.flipNow();
        }
    }
    
    flipAndFlipNow(){
        if(!this.isFlipped){
            this.flipTry();
        }
        this.flipFeatures();
    }
    
    flipFeatures(){
        let featTrans = this.features.get("p1Health");
        this.features.set("p1Health",this.features.get("p2Health"));
        this.features.set("p2Health",featTrans);
        featTrans = this.features.get("p1Deck");
        this.features.set("p1Deck",this.features.get("p2Deck"));
        this.features.set("p2Deck",featTrans);
        featTrans = this.features.get("p1Mana");
        this.features.set("p1Mana",this.features.get("p2Mana"));
        this.features.set("p2Mana",featTrans);
        featTrans = this.features.get("p1RegRes");
        this.features.set("p1RegRes",this.features.get("p2RegRes"));
        this.features.set("p2RegRes",featTrans);
        featTrans = this.features.get("devotion1");
        this.features.set("devotion1",this.features.get("devotion2"));
        this.features.set("devotion2",featTrans);
    }
    
    flipZones(){
        let zoneTrans = this.allZones.get("hand1");
        this.allZones.set("hand1",this.allZones.get("hand2"));
        this.allZones.set("hand2",zoneTrans);
        zoneTrans = this.allZones.get("aura1");
        this.allZones.set("aura1",this.allZones.get("aura2"));
        this.allZones.set("aura2",zoneTrans);
        zoneTrans = this.allZones.get("creature1");
        this.allZones.set("creature1",this.allZones.get("creature2"));
        this.allZones.set("creature2",zoneTrans);
    }

	setZones(){
		for(let zone of this.allZones.values()){
			if(zone instanceof cardCollection){
				zone.setZones();
			}
		}
	}
	
	buildResZones(){
		this.healthRes1 = new healthResZone(BASE_1_DIV,this.features.get("p1Health"),this.features.get("p1RegRes"),this.features.get("p1Deck"));
		this.healthRes2 = new healthResZone(BASE_2_DIV,this.features.get("p2Health"),this.features.get("p2RegRes"),this.features.get("p2Deck"));
		this.p1Mana = new manaZone(MANA_1_DIV,this.features.get("p1Mana"));
		this.p2Mana = new manaZone(MANA_2_DIV,this.features.get("p2Mana"));
		this.allZones.set("healthRes1",new healthResZone(BASE_1_DIV,this.features.get("p1Health"),this.features.get("p1RegRes"),this.features.get("p1Deck")));
		this.allZones.set("healthRes2",new healthResZone(BASE_2_DIV,this.features.get("p2Health"),this.features.get("p2RegRes"),this.features.get("p2Deck")));
		this.allZones.set("p1Mana",new manaZone(MANA_1_DIV,this.features.get("p1Mana")));
		this.allZones.set("p2Mana",new manaZone(MANA_2_DIV,this.features.get("p2Mana")));
        this.allZones.set("p1HUD", new hudZone(HUD_1_DIV,this.features.get("p1Health"),this.features.get("p1RegRes")));
        this.allZones.set("p2HUD", new hudZone(HUD_2_DIV,this.features.get("p2Health"),this.features.get("p2RegRes")));
        this.allZones.set("devotion1",new DevotionZone(DEVOTION_1_DIV,this.features.get("devotion1")));
        this.allZones.set("devotion2",new DevotionZone(DEVOTION_2_DIV,this.features.get("devotion2")));
    
    }
	
	changeFeature(key,val){
		this.features.set(key,val);
	}
	
	changeZone(name,val){
		this.allZones.get(name).setCardsFromCache(val,this.cache);
	}
	
	draw(){
		for(let zone of this.allZones.values()){
			zone.draw();
		}
		this.setupCardClick();
	}
	
	forceRedraw(){
		for(let zone of this.allZones.values()){
			zone.forceRedraw();
		}
		this.setupCardClick();
	}

	getFromCache(data){
		this.loadZoneFromCache(this.allZones.get("hand1"),data.hand1.cards);
		this.loadZoneFromCache(this.allZones.get("hand2"),data.hand2.cards);
		this.loadZoneFromCache(this.allZones.get("aura1"),data.aura1.cards);
		this.loadZoneFromCache(this.allZones.get("aura2"),data.aura2.cards);
		this.loadZoneFromCache(this.allZones.get("creature1"),data.creature1.cards);
		this.loadZoneFromCache(this.allZones.get("creature2"),data.creature2.cards);
	}
	
	loadZoneFromCache(zone,data){
		let ids = [];
		for(let card of data){
			ids.push(card.id);
		}
		zone.setCardsFromCache(ids,cardCache);
	}
	
	pushCard(card, zone, owner){
		switch(zone) {
			case ZoneEnum.HAND:
				if(owner == 1){
					this.allZones.get("hand1").pushCard(card);
				}
				else{
					this.allZones.get("hand2").pushCard(card);
				}
				break;
			case ZoneEnum.AURA:
				if(owner == 1){
					this.allZones.get("aura1").pushCard(card);
				}
				else{
					this.allZones.get("aura2").pushCard(card);
				}
				break;
			case ZoneEnum.CREATURE:
				if(owner == 1){
					this.allZones.get("creature1").pushCard(card);
				}
				else{
					this.allZones.get("creature2").pushCard(card);
				}
				break;
			default:
				console.log("unknown zone in board pushcard");
		}
	}
    
    setupCardClick(){
        if(!isReplayMode){
            $(".card").on("click", function(){
                server.cardClicked($(this).attr("id"));
            });
            HUD_1_DIV.mouseup(function(event){
                mouseSystem.mouseupPlayer(true,event);
            });
            HUD_2_DIV.mouseup(function(event){
                mouseSystem.mouseupPlayer(false,event);
            });
        }
    }
}