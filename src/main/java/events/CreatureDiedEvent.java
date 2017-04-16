package events;

import java.util.List;

import cardgamelibrary.Card;
import cardgamelibrary.Creature;
import cardgamelibrary.Event;
import cardgamelibrary.EventType;

public class CreatureDiedEvent implements Event {

	private Creature creature;

	public CreatureDiedEvent(Creature cr) {
		creature = cr;
	}

	@Override
	public EventType getType() {
		// TODO Auto-generated method stub
		return EventType.CREATURE_DIED;
	}

	@Override
	public List<Card> getAffected() {
		// TODO Auto-generated method stub
		return null;
	}

	public Creature getCreature() {
		return creature;
	}

}
