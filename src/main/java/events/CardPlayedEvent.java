package events;

import cardgamelibrary.Card;
import cardgamelibrary.Event;
import cardgamelibrary.EventType;
import cardgamelibrary.OrderedCardCollection;

public class CardPlayedEvent extends Event {

	private Card									c;
	private OrderedCardCollection	start;
	private OrderedCardCollection	destination;

	public CardPlayedEvent(Card c, OrderedCardCollection start, OrderedCardCollection destination) {
		this.c = c;
		this.start = start;
		this.destination = destination;
	}

	public Card getCard() {
		return c;
	}

	public OrderedCardCollection getStart() {
		return start;
	}

	public OrderedCardCollection getDestination() {
		return destination;
	}

	@Override
	public EventType getType() {
		return EventType.CARD_PLAYED;
	}
}
