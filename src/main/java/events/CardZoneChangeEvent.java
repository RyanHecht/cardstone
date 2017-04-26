package events;

import cardgamelibrary.Card;
import cardgamelibrary.Event;
import cardgamelibrary.EventType;
import cardgamelibrary.OrderedCardCollection;

public class CardZoneChangeEvent implements Event {

	private Card									card;
	private OrderedCardCollection	start;
	private OrderedCardCollection	destination;

	public CardZoneChangeEvent(Card card, OrderedCardCollection destination, OrderedCardCollection start) {
		this.card = card;
		this.start = start;
		this.destination = destination;
	}

	@Override
	public EventType getType() {
		// TODO Auto-generated method stub
		return EventType.CARD_ZONE_CHANGED;
	}

	public Card getCard() {
		return card;
	}

	public OrderedCardCollection getStart() {
		return start;
	}

	public OrderedCardCollection getEnd() {
		return destination;
	}

}
