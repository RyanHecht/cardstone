package events;

import cardgamelibrary.Event;
import cardgamelibrary.EventType;
import cardgamelibrary.Zone;
import templates.ActivatableCard;

public class CardActivatedEvent extends Event {
	private ActivatableCard activated;
	private Zone activatedIn;

	public CardActivatedEvent(ActivatableCard activated, Zone activatedIn) {
		this.activated = activated;
		this.activatedIn = activatedIn;
	}

	public ActivatableCard getActivated() {
		return activated;
	}

	public Zone getActivatedIn() {
		return activatedIn;
	}

	public EventType getType() {
		return EventType.CARD_ACTIVATED;
	}
}
