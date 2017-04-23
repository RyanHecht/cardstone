package cards.templates;

import cardgamelibrary.Effect;
import effects.EmptyEffect;

public interface CanAttackCreature {

	default public Effect onTurnStart(){
		this.setAttacks(1);
		return super.onTurnStart();
	}
	
}
