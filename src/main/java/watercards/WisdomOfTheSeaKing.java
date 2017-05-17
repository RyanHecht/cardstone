package watercards;

import cardgamelibrary.Card;
import cardgamelibrary.CardType;
import cardgamelibrary.ConcatEffect;
import cardgamelibrary.Effect;
import cardgamelibrary.ManaPool;
import cardgamelibrary.SpellCard;
import cardgamelibrary.Zone;
import effects.CardDrawEffect;
import effects.StealDrawEffect;
import game.Player;

public class WisdomOfTheSeaKing extends SpellCard{

	private static final String defaultImage = "images/WisdomOfTheSeaKing.png";
	private static final String defaultName = "Wisdom of the Sea King";
	private static final String defaultText = "Draw 3 cards: 1 from your deck and 2 from your opponents.";
	private static final CardType defaultType = CardType.SPELL;
	private int value;
	public WisdomOfTheSeaKing(Player owner) {
		super(new ManaPool(50, 0, 3, 0, 0, 0), defaultImage, owner, defaultName, defaultText, defaultType);
		value = 0;
	}
	
	public Effect onThisPlayed(Card c, Zone z){
		ConcatEffect ce = new ConcatEffect(this);
		ce.addEffect(new CardDrawEffect(getOwner(),this));
		ce.addEffect(new StealDrawEffect(getOwner(),this));
		ce.addEffect(new StealDrawEffect(getOwner(),this));
		return ce;
	}
	
}
