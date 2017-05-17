package templates;

import cardgamelibrary.Effect;
import cardgamelibrary.Zone;
import effects.EmptyEffect;
import game.Player;

public interface ActivateTargetingPlayer extends ActivatableCard {
	// validates player target selection.
	public boolean playerValidTarget(Player p);

	// when the target is valid, produce some effect.
	// this should usually also play the targetting card (move to appropriate zone
	// i.e. graveyard).
	public Effect impactPlayerTarget(Player target);

	@Override
	public default Effect onPlayerTarget(TargetsPlayer targetter, Player targetted, Zone z) {
		if (targetter.equals(this) && playerValidTarget(targetted)) {
			// the card targetted is a valid target AND the card targetting is this
			// card AND the card is being played from the hand (though this could be
			// any zone).
			// produce specific effect on target.
			return impactPlayerTarget(targetted);
		}
		return EmptyEffect.create();
	}
}
