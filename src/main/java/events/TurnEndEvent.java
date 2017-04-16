package events;

import java.util.List;

import cardgamelibrary.Card;
import cardgamelibrary.Event;
import cardgamelibrary.EventType;

public class TurnEndEvent implements Event {

	@Override
	public EventType getType() {
		return EventType.TURN_END;
	}

	@Override
	public List<Card> getAffected() {
		// TODO Auto-generated method stub
		return null;
	}

}
