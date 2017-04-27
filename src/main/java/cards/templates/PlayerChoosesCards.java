package cards.templates;

import java.util.LinkedList;
import java.util.List;

import cardgamelibrary.Board;
import cardgamelibrary.Card;
import cardgamelibrary.Effect;
import cardgamelibrary.Zone;
import effects.EmptyEffect;

public interface PlayerChoosesCards extends Card {

	@Override
	public default Effect onThisPlayed(Card c, Zone z) {
		assert (c.equals(this));
		return EmptyEffect.create();
	}

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

	public default Effect getChooseEffect(Card thisCard, Card chosenCard) {
		return EmptyEffect.create();
	}
}
