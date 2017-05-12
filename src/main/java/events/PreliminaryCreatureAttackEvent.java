package events;

import cardgamelibrary.CreatureInterface;
import cardgamelibrary.EventType;

public class PreliminaryCreatureAttackEvent extends CreatureAttackEvent{

	public PreliminaryCreatureAttackEvent(CreatureInterface attacker, CreatureInterface target) {
		super(attacker, target);
		// TODO Auto-generated constructor stub
	}
	
	public EventType getType(){
		return EventType.PRELIMINARY_CREATURE_ATTACK;
	}

}
