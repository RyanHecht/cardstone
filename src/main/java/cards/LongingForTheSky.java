package cards;

import cardgamelibrary.Card;
import cardgamelibrary.CardType;
import cardgamelibrary.Effect;
import cardgamelibrary.ElementType;
import cardgamelibrary.ManaPool;
import cardgamelibrary.SpellCard;
import cardgamelibrary.Zone;
import effects.EmptyEffect;
import effects.SummonEffect;
import game.Player;

public class LongingForTheSky extends SpellCard{

	private static final String		defaultImage	= "images/LongingForTheSky.jpg";
	private static final String		defaultName		= "Longing for the sky";
	private static final String		defaultText		= "After playing this and then playing 6 air cards, summon Il-Jun, Sky Child";
	private static final CardType	defaultType		= CardType.SPELL;
	private boolean counting = false;
	private int count;
	
	
	public LongingForTheSky(Player owner) {
		super(new ManaPool(35, 0, 0, 0, 2, 0), defaultImage, owner, defaultName, defaultText, defaultType);
	}
	
	
	public Effect onThisPlayed(Card c, Zone z){
		if(counting){
			
		}
		else{
			counting = true;
			count = 0;
		}
		return EmptyEffect.create();
	}
	
	public Effect onOtherCardPlayed(Card c, Zone z){
		if(c.getOwner().equals(getOwner()) && counting && c.hasElement(ElementType.AIR)){
			count++;
		}
		if(count >= 6){
			counting = false;
			return new SummonEffect(new IlJunSkyChild(getOwner()),Zone.CREATURE_BOARD,this);
		}
		return EmptyEffect.create();
	}
	
}
