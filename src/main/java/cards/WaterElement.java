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

public class WaterElement extends Element {

	private static final ManaPool defaultCost = ManaPool.emptyPool();
	private static final String defaultImage = "images/waterChoices/waterBig.jpg";
	private static final String defaultName = "water";
	private static final String defaultText = "";
	private static final CardType defaultType = CardType.ELEMENT;

	public WaterElement(Player owner) {
		super(defaultCost, defaultImage, owner, defaultName, defaultText, defaultType);
	}

	@Override
	public Effect onThisPlayed(Card c, Zone z) {
		assert (this.equals(c));
		ConcatEffect effect = new ConcatEffect();
		effect.addEffect(new GiveElementEffect(getOwner(),ElementType.AIR,Element.DEFAULT_ELEMENT_GAIN));
		effect.addEffect(new AddToOccEffect(this,getOwner(),Zone.GRAVE,Zone.HAND));
		return effect;
	}

}
