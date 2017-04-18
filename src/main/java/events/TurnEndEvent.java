package events;

import cardgamelibrary.Event;
import cardgamelibrary.EventType;

public class TurnEndEvent implements Event {

	@Override
	public EventType getType() {
		return EventType.TURN_END;
	}

}
