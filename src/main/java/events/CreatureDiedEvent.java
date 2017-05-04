package events;

import cardgamelibrary.CreatureInterface;
import cardgamelibrary.Event;
import cardgamelibrary.EventType;

public class CreatureDiedEvent extends Event {

	private CreatureInterface creature;

	public CreatureDiedEvent(CreatureInterface c) {
		super();
		creature = c;
	}

	@Override
	public EventType getType() {
		// TODO Auto-generated method stub
		return EventType.CREATURE_DIED;
	}

	public CreatureInterface getCreature() {
		return creature;
	}

}
