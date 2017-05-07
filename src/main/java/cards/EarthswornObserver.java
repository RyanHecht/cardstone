package cards;

import cardgamelibrary.CardType;
import cardgamelibrary.Creature;
import cardgamelibrary.CreatureInterface;
import cardgamelibrary.Effect;
import cardgamelibrary.ManaPool;
import cardgamelibrary.Zone;
import effects.CreatureHealthChangeEffect;
import effects.EmptyEffect;
import game.Player;
import templates.CantAttackCreature;

public class EarthswornObserver extends Creature implements CantAttackCreature {

	private static final ManaPool defaultCost = new ManaPool(25, 0, 0, 1, 0, 0);
	private static final String defaultImage = "images/EarthswornObserver.png";
	private static final String defaultName = "Earthsworn Observer";
	private static final String defaultText = "Can't attack. Whenever a friendly minion with Earth in its cost dies, gain 2 health.";
	private static final int defaultHealth = 8;
	private static final int defaultAttack = 0;
	private static final CardType defaultType = CardType.CREATURE;

	public EarthswornObserver(Player owner) {
		super(defaultHealth, defaultAttack, defaultCost, defaultImage, owner, defaultName, defaultText, defaultType);
	}

	@Override
	public Effect onCreatureDeath(CreatureInterface cr, Zone z) {

		if (z == Zone.CREATURE_BOARD && cr.getOwner().equals(getOwner())) {
			return new CreatureHealthChangeEffect(2, this, this);
		}
		return EmptyEffect.create();
	}

}
