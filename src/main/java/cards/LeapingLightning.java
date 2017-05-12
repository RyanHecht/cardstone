package cards;

import cardgamelibrary.Card;
import cardgamelibrary.CardType;
import cardgamelibrary.ConcatEffect;
import cardgamelibrary.Effect;
import cardgamelibrary.ManaPool;
import cardgamelibrary.SpellCard;
import cardgamelibrary.Zone;
import devotions.AirDevotion;
import effects.RandomCreatureDamage;
import game.Player;

public class LeapingLightning extends SpellCard{

	private static final String defaultImage = "images/LeapingLightning.jpg";
	private static final String defaultName = "Leaping Lightning!";
	private static final String defaultText = "Deal damage equal to your storm charge to a random minion, then 1 less damage to another random minion repeatedly, down to 0.";
	private static final CardType defaultType = CardType.SPELL;

	public LeapingLightning(Player owner) {
		super(new ManaPool(45, 0, 0, 0, 3, 0), defaultImage, owner, defaultName, defaultText, defaultType);
	}
	
	public Effect onThisPlayed(Card c, Zone z){
		ConcatEffect ce = new ConcatEffect(this);
		int start = AirDevotion.getLevelOfAir(getOwner().getDevotion());
		for(;start > 0; start--){
			ce.addEffect(new RandomCreatureDamage(true,true,start,this));
		}
		return ce;
	}
	
}
