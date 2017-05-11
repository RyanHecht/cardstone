package cards;

import cardgamelibrary.Card;
import cardgamelibrary.CardType;
import cardgamelibrary.DevotionType;
import cardgamelibrary.Effect;
import cardgamelibrary.ElementType;
import cardgamelibrary.ManaPool;
import cardgamelibrary.SpellCard;
import effects.EmptyEffect;
import effects.GiveElementEffect;
import game.Player;

public class DeepWoodsJade extends SpellCard{

	private static final String defaultImage = "images/DeepWoodsJade.png";
	private static final String defaultName = "Deep Woods Jade";
	private static final String defaultText = "If you are devoted to earth, gain 7 earth.";
	private static final CardType defaultType = CardType.SPELL;

	public DeepWoodsJade(Player owner) {
		super(new ManaPool(0, 0, 0, 0, 1, 0), defaultImage, owner, defaultName, defaultText, defaultType);
	}
	
	public Effect onThisPlayed(Card c){
		if(getOwner().getDevotion().getDevotionType().equals(DevotionType.EARTH)){
			return new GiveElementEffect(getOwner(),ElementType.EARTH,7,this);
		}
		return EmptyEffect.create();
	}
	
}
