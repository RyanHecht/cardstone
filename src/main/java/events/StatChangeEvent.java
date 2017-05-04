package events;

import cardgamelibrary.CreatureInterface;
import cardgamelibrary.Event;
import cardgamelibrary.EventType;

public class StatChangeEvent extends Event {

	private EventType eventType;
	private CreatureInterface target;
	private int amount;

	public StatChangeEvent(EventType type, CreatureInterface target2, int amount) {
		super();
		this.eventType = type;
		this.target = target2;
		this.amount = amount;
	}

	public EventType getType() {
		return eventType;
	}

	public CreatureInterface getTarget() {
		return target;
	}

	public int getAmount() {
		return amount;
	}

}
