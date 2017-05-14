package cards;

import cardgamelibrary.Board;
import cardgamelibrary.Card;
import cardgamelibrary.CardType;
import cardgamelibrary.ConcatEffect;
import cardgamelibrary.Effect;
import cardgamelibrary.ManaPool;
import cardgamelibrary.SpellCard;
import cardgamelibrary.Zone;
import effects.AddToOccEffect;
import effects.EffectMaker;
import game.Player;

public class RainingCatsAndDogs extends SpellCard{
	
	private static final String		defaultImage	= "images/RainingCatsAndDogs.png";
	private static final String		defaultName		= "Raining Cats and Dogs";
	private static final String		defaultText		= "Place all creatures that cost 10 or less from your hand onto the board.";
	private static final CardType	defaultType		= CardType.SPELL;
	
	public RainingCatsAndDogs(Player owner) {
		super(new ManaPool(30, 0, 3, 0, 0, 0), defaultImage, owner, defaultName, defaultText, defaultType);
	}
	
	public Effect onThisPlayed(Card c, Zone z){
		return new EffectMaker((Board b) -> {
			ConcatEffect ce = new ConcatEffect(this);
			for(Card card : b.getOcc(getOwner(), Zone.HAND)){
				ce.addEffect(new AddToOccEffect(c,getOwner(),Zone.CREATURE_BOARD,Zone.HAND,this));
			}
			return ce;
		},this);
	}
	
	
	
}
