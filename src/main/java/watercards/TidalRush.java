package watercards;

import cardgamelibrary.Card;
import cardgamelibrary.CardType;
import cardgamelibrary.ConcatEffect;
import cardgamelibrary.CreatureInterface;
import cardgamelibrary.Effect;
import cardgamelibrary.ManaPool;
import cardgamelibrary.SpellCard;
import cardgamelibrary.Zone;
import effects.AddToOccEffect;
import effects.ApplyEffect;
import effects.CardDamageEffect;
import game.Player;
import templates.TargetsOtherCard;
import templates.decorators.CantAttackForTurnsCreature;

public class TidalRush extends SpellCard implements TargetsOtherCard{

	private static final String defaultImage = "images/TidalRush.jpg";
	private static final String defaultName = "Tidal Rush";
	private static final String defaultText = "Return target minion to its owners hand.";
	private static final CardType defaultType = CardType.SPELL;
	private int value;
	public TidalRush(Player owner) {
		super(new ManaPool(10, 0, 1, 0, 0, 0), defaultImage, owner, defaultName, defaultText, defaultType);
		value = 0;
	}
	
	@Override
	public boolean cardValidTarget(Card card, Zone targetIn) {
		return targetIn.equals(Zone.CREATURE_BOARD);
	}

	@Override
	public Effect impactCardTarget(Card target, Zone targetIn) {
		ConcatEffect ce = new ConcatEffect(this);
		ce.addEffect(new AddToOccEffect(target,target.getOwner(),Zone.HAND,targetIn,this));
		return ce;
	}
	
}
