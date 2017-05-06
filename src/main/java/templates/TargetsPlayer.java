package templates;

import cardgamelibrary.Card;
import cardgamelibrary.Effect;
import cardgamelibrary.Zone;
import effects.EmptyEffect;
import game.Player;

public interface TargetsPlayer extends Card {
	// validates player target selection.
	public boolean playerValidTarget(Player p);

	// when the target is valid, produce some effect.
	// this should usually also play the targetting card (move to appropriate zone
	// i.e. graveyard).
	default public Effect impactPlayerTarget(Player target) {
		return EmptyEffect.create();
	}

	@Override
	public default Effect onPlayerTarget(TargetsPlayer targetter, Player targetted, Zone z) {
		if (targetter.equals(this) && playerValidTarget(targetted) && z == Zone.HAND) {
			// the card targetted is a valid target AND the card targetting is this
			// card AND the card is being played from the hand (though this could be
			// any zone).
			// produce specific effect on target.
			return impactPlayerTarget(targetted);
		}
		return EmptyEffect.create();
	}
}
