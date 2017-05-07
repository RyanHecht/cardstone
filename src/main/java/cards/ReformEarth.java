package cards;

import cardgamelibrary.AuraCard;
import cardgamelibrary.CardType;
import cardgamelibrary.Creature;
import cardgamelibrary.CreatureInterface;
import cardgamelibrary.Effect;
import cardgamelibrary.ElementType;
import cardgamelibrary.ManaPool;
import cardgamelibrary.Zone;
import effects.CardDrawEffect;
import effects.EmptyEffect;
import game.Player;

public class ReformEarth extends AuraCard{

	private static final String defaultImage = "images/ReformEarth.jpg";
	private static final String defaultName = "Reform Earth";
	private static final String defaultText = "For every third friendly earth creature that dies, draw a card";
	private static final CardType defaultType = CardType.AURA;
	private int deadCount;

	public ReformEarth(Player owner) {
		super(new ManaPool(15, 0, 0, 2, 0, 0), defaultImage, owner, defaultName, defaultText, defaultType);
		this.deadCount = 0;
	}
	
	public Effect onThisPlayed(Zone z){
		deadCount = 0;
		return EmptyEffect.create();
	}
	
	public Effect onCreatureDeath(CreatureInterface c, Zone z){
		if(z == Zone.AURA_BOARD && c.getOwner() == this.getOwner() && c.getCost().getElement(ElementType.EARTH) > 0){
			deadCount++;
			if(deadCount >= 3){
				return new CardDrawEffect(getOwner(),this);
			}
		}
		return EmptyEffect.create();
	}
	
}
