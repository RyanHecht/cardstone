package cards;

import cardgamelibrary.Board;
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

public class RockShock extends SpellCard implements TargetsOtherCard {

	public RockShock(Player owner) {
		super(new ManaPool(15, 0, 0, 2, 0, 0), defaultImage, owner, defaultName, defaultText, defaultType);
	}

	private static final String defaultImage = "images/RockShock.jpg";
	private static final String defaultName = "Rock Shock";
	private static final String defaultText = "Deal two damage to target minion and prevent it from attacking next turn.";
	private static final CardType defaultType = CardType.SPELL;

	public boolean cardValidTarget(Card card, Zone z) {
		if (card.getType() == CardType.CREATURE && z == Zone.CREATURE_BOARD) {
			return true;
		}
		return false;
	}


	public Effect impactCardTarget(Card target, Zone zone) {
		ConcatEffect ce = new ConcatEffect(this);
		ce.addEffect(new CardDamageEffect(this,(CreatureInterface)target,2));
		ce.addEffect(new ApplyEffect(target, new CantAttackForTurnsCreature((CreatureInterface) target,2),this));
		return ce;
	}

}
