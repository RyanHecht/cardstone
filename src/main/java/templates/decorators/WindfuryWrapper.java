package templates.decorators;

import cardgamelibrary.CreatureInterface;
import cardgamelibrary.Effect;
import cardgamelibrary.Zone;
import game.Player;

public class WindfuryWrapper extends CreatureWrapper{

	public WindfuryWrapper(CreatureInterface internal){
		super(internal);
	}
	
	public void allowAttack(){
		this.setAttacks(2);
	}
	
}
