package events;

import cardgamelibrary.Card;
import cardgamelibrary.Event;
import cardgamelibrary.EventType;
import cardgamelibrary.Zone;
import templates.TargetsOtherCard;

public class CardTargetedEvent extends Event {

	private TargetsOtherCard	targetter;
	private Card							targeted;
	private Zone							targetIn;

	public CardTargetedEvent(TargetsOtherCard targetter, Card targeted, Zone targetIn) {
		super();
		this.targetter = targetter;
		this.targeted = targeted;
		this.targetIn = targetIn;
	}

	public TargetsOtherCard getTargetter() {
		return targetter;
	}

	public Card getTargeted() {
		return targeted;
	}

	public Zone getTargetZone() {
		return targetIn;
	}

	@Override
	public EventType getType() {
		// TODO Auto-generated method stub
		return EventType.CARD_TARGETED;
	}

}
