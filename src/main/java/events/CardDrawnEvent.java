package events;

import java.util.List;

import cardgamelibrary.Card;
import cardgamelibrary.Event;
import cardgamelibrary.EventType;

public class CardDrawnEvent implements Event {

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
