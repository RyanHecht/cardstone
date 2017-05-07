package cards;

import cardgamelibrary.Board;
import cardgamelibrary.Card;
import cardgamelibrary.CardType;
import cardgamelibrary.ConcatEffect;
import cardgamelibrary.Effect;
import cardgamelibrary.Element;
import cardgamelibrary.ElementType;
import cardgamelibrary.ManaPool;
import cardgamelibrary.Zone;
import effects.AddToOccEffect;
import effects.GiveElementEffect;
import game.Player;

public class BalanceElement extends Element {

	private static final ManaPool defaultCost = ManaPool.emptyPool();
	private static final String defaultImage = "images/waterChoices/balanceBig.jpg";
	private static final String defaultName ="balance";
	private static final String defaultText = "";
	private static final CardType defaultType = CardType.ELEMENT;

	public BalanceElement(Player owner) {
		super(defaultCost, defaultImage, owner, defaultName, defaultText, defaultType);
	}

	@Override
	public Effect onThisPlayed(Card c, Zone z) {
		assert (this.equals(c));
		ConcatEffect effect = new ConcatEffect(this);
		effect.addEffect(new GiveElementEffect(getOwner(),ElementType.BALANCE,Element.DEFAULT_ELEMENT_GAIN,this));
		effect.addEffect(new AddToOccEffect(this,getOwner(),Zone.GRAVE,Zone.HAND,this));
		return effect;
	}

}
