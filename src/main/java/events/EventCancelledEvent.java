package events;

import cardgamelibrary.Card;
import cardgamelibrary.Event;
import cardgamelibrary.EventType;

public class EventCancelledEvent extends Event{

	private Card canceller;
	private Event cancelled;

	@Override
	public EventType getType() {
		return EventType.EVENT_CANCELLED;
	}
	
	public EventCancelledEvent(Event cancelled, Card canceller){
		super();
		this.canceller = canceller;
		this.cancelled = cancelled;
	}
	
	public Card getCanceller(){
		return canceller;
	}
	
	public Event getCancelled(){
		return cancelled;
	}

	
	
}
