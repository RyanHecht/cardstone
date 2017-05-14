package templates;

import java.util.LinkedList;
import java.util.List;

import cardgamelibrary.Board;
import cardgamelibrary.Card;
import cardgamelibrary.Effect;
import cardgamelibrary.Zone;
import effects.EmptyEffect;

public interface PlayerChoosesCards extends ChooseResponderCard {

	/**
	 * Gets the options the person who played this card must choose from.
	 *
	 * @param board
	 *          the board we are searching for options.
	 * @return a list of things we want the player to choose from.
	 */
	default public List<Card> getOptions(Board board) {
		return new LinkedList<Card>();
	}

	/**
	 * Gets the number of choices the player will have to make.
	 *
	 * @return the number of choices the player will make.
	 */
	public int getNumChoices();
}
