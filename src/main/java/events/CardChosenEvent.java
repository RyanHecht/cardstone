package events;

import cardgamelibrary.Card;
import cardgamelibrary.Event;
import cardgamelibrary.EventType;

public class CardChosenEvent implements Event {

	private Card	chooser;
	private Card	chosen;

	public CardChosenEvent(Card chooser, Card chosen) {
		this.chooser = chooser;
		this.chosen = chosen;
	}

	/**
	 * Gets the card that prompted the choice.
	 * 
	 * @return the card that prompted the choice.
	 */
	public Card getChooser() {
		return chooser;
	}

	/**
	 * Gets the card chosen.
	 * 
	 * @return the card chosen by the player.
	 */
	public Card getChosen() {
		return chosen;
	}

	@Override
	public EventType getType() {
		// TODO Auto-generated method stub
		return EventType.CARD_CHOSEN;
	}

}
