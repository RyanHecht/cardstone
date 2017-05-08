package cards;

import cardgamelibrary.Card;
import cardgamelibrary.CardType;
import cardgamelibrary.CreatureInterface;
import cardgamelibrary.Effect;
import cardgamelibrary.ManaPool;
import cardgamelibrary.SpellCard;
import cardgamelibrary.Zone;
import effects.ApplyEffect;
import game.Player;
import templates.TargetsOtherCard;
import templates.decorators.OnlyTakesXDamageCreature;

public class Petrify extends SpellCard implements TargetsOtherCard {
	private static final String defaultImage = "images/Petrify.jpg";
	private static final String defaultName = "Petrify";
	private static final String defaultText = "Target Creature gains taunt and can only take two damage at a time.";
	private static final CardType defaultType = CardType.SPELL;

	public Petrify(Player owner) {
		super(new ManaPool(10, 0, 0, 1, 0, 0), defaultImage, owner, defaultName, defaultText, defaultType);
	}

	@Override
	public boolean cardValidTarget(Card card, Zone targetIn) {
		return card.getType() == CardType.CREATURE && targetIn == Zone.CREATURE_BOARD;
	}

	@Override
	public Effect impactCardTarget(Card target, Zone targetIn) {
		CreatureInterface cr = new OnlyTakesXDamageCreature((CreatureInterface) target, 2);
		ApplyEffect e = new ApplyEffect(target, cr, this);
		return e;
	}
}
