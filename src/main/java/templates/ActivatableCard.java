package templates;

import cardgamelibrary.Card;
import cardgamelibrary.Effect;
import cardgamelibrary.Zone;
import effects.EmptyEffect;

public interface ActivatableCard extends Card {


	// checks to see if a card can be activated
	// at a given moment.
	default public boolean canBeActivated() {
		return getOwner().validateCost(getActivationCost());
	}

	@Override
	public default Effect onCardActivation(ActivatableCard c, Zone activatedIn, Zone z) {
		if (c.equals(this) && activatedIn == z && (z == Zone.CREATURE_BOARD || z == Zone.AURA_BOARD)) {
			assert (this.canBeActivated());

			// pay activation cost of card.
			getOwner().payCost(getActivationCost());
			return onThisActivated();
		}
		return EmptyEffect.create();
	}

	public Effect onThisActivated();

	boolean canBeActivated(Zone z);
}
