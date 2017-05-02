package events;

import cardgamelibrary.Card;
import cardgamelibrary.Event;
import cardgamelibrary.EventType;
import cards.templates.TargetsOtherCard;

public class CardTargetedEvent extends Event {

	private TargetsOtherCard	targetter;
	private Card							targeted;

	public CardTargetedEvent(TargetsOtherCard targetter, Card targeted) {
		this.targetter = targetter;
		this.targeted = targeted;
	}

	public TargetsOtherCard getTargetter() {
		return targetter;
	}

	public Card getTargeted() {
		return targeted;
	}

	@Override
	public EventType getType() {
		// TODO Auto-generated method stub
		return EventType.CARD_TARGETED;
	}

}
