package templates;

import cardgamelibrary.Card;
import cardgamelibrary.Creature;
import cardgamelibrary.Effect;
import cardgamelibrary.Zone;
import effects.EmptyEffect;

public interface OnOwnDeathCard extends Card {

	@Override
	default public Effect onCreatureDeath(Creature cr, Zone z) {
		if (cr.equals(this)) {
			return this.onDeathEffect(z);
		} else {
			return EmptyEffect.create();
		}
	}

	public Effect onDeathEffect(Zone z);

}
