package events;

import cardgamelibrary.Card;
import cardgamelibrary.Event;
import cardgamelibrary.EventType;
import templates.ChooseResponderCard;
import templates.PlayerChoosesCards;

public class CardChosenEvent extends Event {

	private ChooseResponderCard	chooser;
	private Card								chosen;

	public CardChosenEvent(ChooseResponderCard chooserCard, Card chosen) {
		super();
		this.chooser = chooserCard;
		this.chosen = chosen;
	}

	/**
	 * Gets the card that prompted the choice.
	 *
	 * @return the card that prompted the choice.
	 */
	public ChooseResponderCard getChooser() {
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
