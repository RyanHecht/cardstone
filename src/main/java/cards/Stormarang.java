package cards;

import cardgamelibrary.Card;
import cardgamelibrary.CardType;
import cardgamelibrary.Effect;
import cardgamelibrary.ManaPool;
import cardgamelibrary.SpellCard;
import cardgamelibrary.Zone;
import effects.AddToOccEffect;
import effects.SummonEffect;
import game.Player;

public class Stormarang extends SpellCard{

	private static final String		defaultImage	= "images/Boomarock.jpg";
	private static final String		defaultName		= "Stormarang";
	private static final String		defaultText		= "On play, return this to your hand.";
	private static final CardType	defaultType		= CardType.SPELL;
	
	public Stormarang(Player owner) {
		super(new ManaPool(10, 0, 0, 0, 1, 0), defaultImage, owner, defaultName, defaultText, defaultType);
	}
	
	public Effect onThisPlayed(Card c, Zone z){
		return new SummonEffect(this.getNewInstanceOf(getOwner()),Zone.HAND,this);
	}
	
}
