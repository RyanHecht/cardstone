package events;

import cardgamelibrary.Creature;
import cardgamelibrary.Event;
import cardgamelibrary.EventType;

public class CreatureDiedEvent extends Event {

	private Creature creature;

	public CreatureDiedEvent(Creature cr) {
		creature = cr;
	}

	@Override
	public EventType getType() {
		// TODO Auto-generated method stub
		return EventType.CREATURE_DIED;
	}

	public Creature getCreature() {
		return creature;
	}

}
