package cards.templates;

import cardgamelibrary.Card;
import cardgamelibrary.Effect;
import effects.EmptyEffect;

public interface CantAttackCreature extends Card {

	default public Effect onTurnStart() {
		return EmptyEffect.create();
	}
}
