package templates;

import cardgamelibrary.Card;
import cardgamelibrary.Effect;
import cardgamelibrary.Zone;
import effects.EmptyEffect;

public interface ActivatableCard extends Card {


	@Override
	public default Effect onCardActivation(Card c, Zone z) {
		if (c.equals(this) && (z == Zone.CREATURE_BOARD || z == Zone.AURA_BOARD)) {
			assert (this.canBeActivated(z));
			return onThisActivated();
		}
		return EmptyEffect.create();
	}

	public Effect onThisActivated();

	boolean canBeActivated(Zone z);
}
