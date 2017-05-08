package events;

import cardgamelibrary.Card;
import cardgamelibrary.Event;
import cardgamelibrary.EventType;
import cardgamelibrary.OrderedCardCollection;

public class CardPlayedEvent extends Event {

	private Card									c;
	private OrderedCardCollection	start;

	public CardPlayedEvent(Card c, OrderedCardCollection start) {
		super();
		this.c = c;
		this.start = start;
	}

	public Card getCard() {
		return c;
	}

	public OrderedCardCollection getStart() {
		return start;
	}


	@Override
	public EventType getType() {
		return EventType.CARD_PLAYED;
	}
}
