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

public class EarthElement extends Element {

	private static final String defaultImage = "images/waterChoices/fireBig.jpg";
	private static final String defaultName = "earth";
	private static final String defaultText = "";
	private static final CardType defaultType = CardType.ELEMENT;

	public EarthElement(Player owner) {
		super(ManaPool.emptyPool(), defaultImage, owner, defaultName, defaultText, defaultType);
	}

	@Override
	public Effect onThisPlayed(Card c, Zone z) {
		assert (this.equals(c));
		ConcatEffect effect = new ConcatEffect(this);
		effect.addEffect(new GiveElementEffect(getOwner(),ElementType.EARTH,Element.DEFAULT_ELEMENT_GAIN,this));
		effect.addEffect(new AddToOccEffect(this,getOwner(),Zone.GRAVE,Zone.HAND,this));
		return effect;
	}

}
