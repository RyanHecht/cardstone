package events;

import java.util.List;

import cardgamelibrary.Card;
import cardgamelibrary.Event;
import cardgamelibrary.EventType;

public class CreatureDiesEvent implements Event {

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

}
