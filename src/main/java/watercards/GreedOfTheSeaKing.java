package watercards;

import cardgamelibrary.Board;
import cardgamelibrary.Card;
import cardgamelibrary.CardType;
import cardgamelibrary.ConcatEffect;
import cardgamelibrary.Effect;
import cardgamelibrary.ElementType;
import cardgamelibrary.ManaPool;
import cardgamelibrary.SpellCard;
import cardgamelibrary.Zone;
import effects.AddToOccCrossPlayerEffect;
import effects.EffectMaker;
import effects.GiveElementEffect;
import game.Player;

public class GreedOfTheSeaKing extends SpellCard{

	private static final String defaultImage = "images/GreedOfTheSeaKing.png";
	private static final String defaultName = "Greed of the Sea King";
	private static final String defaultText = "Steal your opponents element and 2 element cards from their hand.";
	private static final CardType defaultType = CardType.SPELL;
	private int value;
	public GreedOfTheSeaKing(Player owner) {
		super(new ManaPool(50, 0, 3, 0, 0, 0), defaultImage, owner, defaultName, defaultText, defaultType);
		value = 0;
	}
	
	public Effect onThisPlayed(Card c, Zone z){
		return new EffectMaker((Board b) -> {
			ConcatEffect ce = new ConcatEffect(this);
			Player opponent = b.getOpposingPlayer(getOwner());
			for(ElementType et : ElementType.values()){
				int opp = opponent.getElem(et);
				opponent.setElement(et, 0);
				ce.addEffect(new GiveElementEffect(getOwner(),et,opp,this));
			}
			int taken = 0;
			for(Card card : b.getOcc(opponent, Zone.HAND)){
				if(taken < 2 && card.getType().equals(CardType.ELEMENT)){
					ce.addEffect(new AddToOccCrossPlayerEffect(card,opponent,getOwner(),
							Zone.HAND,Zone.HAND,this));
				}
			}
			return ce;
		},this);
	}
	
}
