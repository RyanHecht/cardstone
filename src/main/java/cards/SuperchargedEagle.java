package cards;

import cardgamelibrary.Card;
import cardgamelibrary.CardType;
import cardgamelibrary.Creature;
import cardgamelibrary.CreatureInterface;
import cardgamelibrary.Effect;
import cardgamelibrary.ManaPool;
import cardgamelibrary.Zone;
import devotions.AirDevotion;
import effects.CreatureAttackChangeEffect;
import game.Player;

public class SuperchargedEagle extends Creature{

	private static final String		defaultImage	= "images/SuperchargedEagle.png";
	private static final String		defaultName		= "Supercharged Eagle";
	private static final String		defaultText		= "Haste. On play, gain attack equal to your storm charge times 2.";
	private static final int			defaultHealth	= 1;
	private static final int			defaultAttack	= 1;
	private static final CardType	defaultType		= CardType.CREATURE;


	public SuperchargedEagle(Player owner) {
		super(defaultHealth, defaultAttack, new ManaPool(60, 0, 0, 3, 0, 0), defaultImage, owner, defaultName, defaultText, defaultType);
	}
	
	
	public Effect onThisPlayed(Card c, Zone z){
		return new CreatureAttackChangeEffect(AirDevotion.getLevelOfAir(getOwner().getDevotion())
				, this,this);
	}
	
}
