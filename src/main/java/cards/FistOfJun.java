package cards;

import cardgamelibrary.Card;
import cardgamelibrary.CardType;
import cardgamelibrary.ConcatEffect;
import cardgamelibrary.CreatureInterface;
import cardgamelibrary.Effect;
import cardgamelibrary.ManaPool;
import cardgamelibrary.SpellCard;
import cardgamelibrary.Zone;
import devotions.AirDevotion;
import effects.CardDamageEffect;
import effects.CardDrawEffect;
import game.Player;
import templates.TargetsOtherCard;

public class FistOfJun extends SpellCard implements TargetsOtherCard{

	private static final String defaultImage = "images/FistOfJun.jpg";
	private static final String defaultName = "Fist of Jun";
	private static final String defaultText = "Draw a card. Deal damage to target creature equal to your storm count.";
	private static final CardType defaultType = CardType.SPELL;

	public FistOfJun(Player owner) {
		super(new ManaPool(5, 0, 0, 0, 1, 0), defaultImage, owner, defaultName, defaultText, defaultType);
	}

	@Override
	public boolean cardValidTarget(Card card, Zone targetIn) {
		return card.getType().equals(CardType.CREATURE) && targetIn.equals(Zone.CREATURE_BOARD);
	}

	@Override
	public Effect impactCardTarget(Card target, Zone targetIn) {
		ConcatEffect ce = new ConcatEffect(this);
		ce.addEffect(new CardDrawEffect(getOwner(),this));
		if(target.isA(CreatureInterface.class)){
			ce.addEffect(new CardDamageEffect(this,(CreatureInterface)target,AirDevotion.getLevelOfAir(getOwner().getDevotion())));
		}
		return ce;
	}
	
}
