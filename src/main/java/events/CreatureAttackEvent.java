package events;

import cardgamelibrary.CreatureInterface;
import cardgamelibrary.Event;
import cardgamelibrary.EventType;

public class CreatureAttackEvent extends Event {
	private CreatureInterface attacker;
	private CreatureInterface target;

	public CreatureAttackEvent(CreatureInterface attacker2, CreatureInterface target2) {
		super();
		this.attacker = attacker2;
		this.target = target2;
	}

	public CreatureInterface getAttacker() {
		return attacker;
	}

	public CreatureInterface getTarget() {
		return target;
	}

	@Override
	public EventType getType() {
		// TODO Auto-generated method stub
		return EventType.CREATURE_ATTACKED;
	}

}
