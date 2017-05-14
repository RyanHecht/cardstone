package templates.decorators;

import cardgamelibrary.Card;
import cardgamelibrary.CreatureInterface;
import cardgamelibrary.Effect;
import cardgamelibrary.Zone;

public class HasteCreature extends CreatureWrapper{

	public HasteCreature(CreatureInterface internal){
		super(internal);
	}
	
	public Effect onThisPlayed(Card c, Zone z){
		creatureInternal.allowAttack();
		return internal.onThisPlayed(c, z);
	}
	
}
