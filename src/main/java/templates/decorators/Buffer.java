package templates.decorators;

import cardgamelibrary.CreatureInterface;

public interface Buffer {

	default public int getAttackBuff(CreatureInterface c){
		return getBuff(c);
	}
	
	default public int getHealthBuff(CreatureInterface c){
		return getBuff(c);
	}
	
	public int getBuff(CreatureInterface c);
	
}
