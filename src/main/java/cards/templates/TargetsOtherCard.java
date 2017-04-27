package cards.templates;

import cardgamelibrary.Card;
import cardgamelibrary.Effect;
import cardgamelibrary.Zone;
import effects.EmptyEffect;

public interface TargetsOtherCard extends Card {

	// validates target selection.
	public boolean isValidTarget(Card card);

	// when the target is valid, produce some effect.
	// this should usually also play the targetting card (move to appropriate zone
	// i.e. graveyard).
	default public Effect impactTarget(Card target) {
		return EmptyEffect.create();
	}

	@Override
	public default Effect onCardTarget(TargetsOtherCard targetter, Card targetted, Zone z) {
		if (targetter.equals(this) && isValidTarget(targetted) && z == Zone.HAND) {
			// the card targetted is a valid target AND the card targetting is this
			// card AND the card is being played from the hand (though this could be
			// any zone).

			// pay cost of the card.
			getOwner().payCost(getCost());

			// produce specific effect on target.
			return impactTarget(targetted);
		}
		return EmptyEffect.create();
	}
}
