package effects;

import cardgamelibrary.Board;
import cardgamelibrary.Card;
import cardgamelibrary.Effect;
import cardgamelibrary.OrderedCardCollection;

public class CardZoneChangeEffect implements Effect {

	private Card									c;
	private OrderedCardCollection	destination;
	private OrderedCardCollection	start;

	public CardZoneChangeEffect(Card c, OrderedCardCollection destination, OrderedCardCollection start) {
		this.c = c;
		this.destination = destination;
		this.start = start;
	}

	@Override
	public void apply(Board board) {
		// TODO Auto-generated method stub
		board.addCardToOcc(c, destination, start);
	}

}
