package cards.templates;

import cardgamelibrary.Card;
import cardgamelibrary.Effect;

public interface TargetsOtherCard {

	// validates target selection.
	public boolean isValidTarget(Card card);

	// when the target is valid, produce some effect.
	public Effect onTargetedPlay(Card target);
}
