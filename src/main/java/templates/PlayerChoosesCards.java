package templates;

import java.util.LinkedList;
import java.util.List;

import cardgamelibrary.Board;
import cardgamelibrary.Card;
import cardgamelibrary.Effect;
import cardgamelibrary.Zone;
import effects.EmptyEffect;

public interface PlayerChoosesCards extends Card {

	@Override
	default public Effect onThisPlayed(Card c, Zone z) {
		return (Board board) -> {

			// send card to graveyard.
			board.addCardToOcc(c, board.getOcc(getOwner(), Zone.GRAVE), board.getOcc(getOwner(), Zone.HAND));
		};
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

	/**
	 * Gets the number of choices the player will have to make.
	 *
	 * @return the number of choices the player will make.
	 */
	public int getNumChoices();

	@Override
	default public Effect onCardChosen(PlayerChoosesCards chooser, Card chosen, Zone z) {
		if (this.equals(chooser)) {
			return getChooseEffect(chooser, chosen);
		}
		return EmptyEffect.create();
	}

	/**
	 * Gets some effect associated with the card the player has chosen.
	 *
	 * @param thisCard
	 *          this card.
	 * @param chosenCard
	 *          the card the user chose.
	 * @return an effect describing how the game state should be changed based on
	 *         the user's choice of card.
	 */
	public default Effect getChooseEffect(PlayerChoosesCards thisCard, Card chosenCard) {
		return EmptyEffect.create();
	}
}
