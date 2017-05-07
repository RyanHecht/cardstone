package cards;

import cardgamelibrary.Card;
import cardgamelibrary.CardType;
import cardgamelibrary.Effect;
import cardgamelibrary.ManaPool;
import cardgamelibrary.SpellCard;
import cardgamelibrary.Zone;
import effects.AddToOccEffect;
import game.Player;

public class Boomarock extends SpellCard{

	private static final String		defaultImage	= "images/Boomarock.jpg";
	private static final String		defaultName		= "Boomarock";
	private static final String		defaultText		= "On play, return this to your hand.";
	private static final CardType	defaultType		= CardType.SPELL;
	
	public Boomarock(Player owner) {
		super(new ManaPool(5, 0, 0, 1, 0, 0), defaultImage, owner, defaultName, defaultText, defaultType);
	}
	
	public Effect onThisPlayed(Card c, Zone z){
		return new AddToOccEffect(this, getOwner(), Zone.GRAVE, Zone.HAND, this);
	}
	
}
