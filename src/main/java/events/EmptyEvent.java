package events;

import cardgamelibrary.Event;
import cardgamelibrary.EventType;

public class EmptyEvent extends Event{

	public EmptyEvent(){
		super();
	}
	
	@Override
	public EventType getType() {
		return EventType.EMPTY;
	}

}
