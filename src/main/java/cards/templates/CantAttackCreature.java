package cards.templates;

import cardgamelibrary.Effect;
import effects.EmptyEffect;

public interface CantAttackCreature {

	default public Effect onTurnStart(){
		return EmptyEffect.create();
	}
	
}
