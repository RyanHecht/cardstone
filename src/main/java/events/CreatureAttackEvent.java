package events;

import cardgamelibrary.Creature;
import cardgamelibrary.Event;
import cardgamelibrary.EventType;

public class CreatureAttackEvent implements Event {
	private Creature	attacker;
	private Creature	target;

	public CreatureAttackEvent(Creature attacker, Creature target) {
		this.attacker = attacker;
		this.target = target;
	}

	public Creature getAttacker() {
		return attacker;
	}

	public Creature getTarget() {
		return target;
	}

	@Override
	public EventType getType() {
		// TODO Auto-generated method stub
		return EventType.CREATURE_ATTACKED;
	}

}
