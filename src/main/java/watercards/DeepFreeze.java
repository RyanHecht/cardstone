package watercards;

import cardgamelibrary.Card;
import cardgamelibrary.CardType;
import cardgamelibrary.ConcatEffect;
import cardgamelibrary.CreatureInterface;
import cardgamelibrary.Effect;
import cardgamelibrary.ManaPool;
import cardgamelibrary.SpellCard;
import cardgamelibrary.Zone;
import effects.ApplyEffect;
import effects.CardDamageEffect;
import game.Player;
import templates.TargetsOtherCard;
import templates.decorators.CantAttackForTurnsCreature;

public class DeepFreeze extends SpellCard implements TargetsOtherCard{

	private static final String defaultImage = "images/DeepFreeze.jpg";
	private static final String defaultName = "Deep Freeze";
	private static final String defaultText = "Immobilize an enemy minion for 2 turns and deal 2 damage to it.";
	private static final CardType defaultType = CardType.SPELL;
	private int value;
	public DeepFreeze(Player owner) {
		super(new ManaPool(20, 0, 1, 0, 0, 0), defaultImage, owner, defaultName, defaultText, defaultType);
		value = 0;
	}
	
	@Override
	public boolean cardValidTarget(Card card, Zone targetIn) {
		return targetIn.equals(Zone.CREATURE_BOARD);
	}

	@Override
	public Effect impactCardTarget(Card target, Zone targetIn) {
		ConcatEffect ce = new ConcatEffect(this);
		ce.addEffect(new CardDamageEffect(this,(CreatureInterface) target,2));
		CantAttackForTurnsCreature caftc = new CantAttackForTurnsCreature((CreatureInterface) target, 4);
		ce.addEffect(new ApplyEffect(target,caftc,this));
		return ce;
	}
	
}
