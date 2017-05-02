package events;

import cardgamelibrary.Card;
import cardgamelibrary.Event;
import cardgamelibrary.EventType;

public class CardDrawnEvent extends Event {

	private Card drawn;

	public CardDrawnEvent(Card drawn) {
		this.drawn = drawn;
	}

	@Override
	public EventType getType() {
		return EventType.CARD_DRAWN;
	}

	public Card getDrawn() {
		return drawn;
	}

}
