package cards;

import cardgamelibrary.Card;
import cardgamelibrary.CardType;
import cardgamelibrary.ConcatEffect;
import cardgamelibrary.Effect;
import cardgamelibrary.ManaPool;
import cardgamelibrary.SpellCard;
import cardgamelibrary.Zone;
import devotions.AirDevotion;
import effects.EmptyEffect;
import effects.SummonEffect;
import game.Player;

public class DragonstoneOfTheStorm extends SpellCard{

	private static final String		defaultImage			= "images/DragonstoneOfTheStorm.jpg";
	private static final String		defaultName			= "Dragonstone of the Storm";
	private static final String		defaultText			= "If you have at least 15 storm charge, summon the Storm Dragon.";
	
	public DragonstoneOfTheStorm(Player owner) {
		super(new ManaPool(10, 0, 0, 0, 1, 0), defaultImage, owner, defaultName, defaultText, CardType.CREATURE);
	}
	
	public Effect onThisPlayed(Card c, Zone z){
		if(AirDevotion.getLevelOfAir(c.getOwner().getDevotion()) >= 15){
			ConcatEffect ce = new ConcatEffect(this);
			StormDragon sd = new StormDragon(getOwner());
			ce.addEffect(new SummonEffect(sd,Zone.CREATURE_BOARD,this));
			ce.addEffect(sd.onThisPlayed(sd, Zone.CREATURE_BOARD));
		}
		return EmptyEffect.create();
	}
	
}
