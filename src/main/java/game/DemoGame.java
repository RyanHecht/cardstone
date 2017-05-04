package game;

import java.util.LinkedList;
import java.util.List;

import com.google.gson.JsonObject;

public class DemoGame extends Game {

	private static final int	AI_ID					= -10;

	private int								currentAction	= 0;

	public DemoGame(int playerOneId) {
		// superclass constructor with "true" flag passed to indicate that it is a
		// tutorial.
		super(getFirstPlayerDeck(), getSecondPlayerDeck(), playerOneId, AI_ID, true);
	}

	@Override
	public void handleCardTargeted(JsonObject userInput, int playerId) {

	}

	@Override
	public void handlePlayerTargeted(JsonObject userInput, int playerId) {

	}

	@Override
	public void handleChosen(JsonObject userInput, int playerId) {

	}

	@Override
	public void handleCardPlayed(JsonObject userInput, int playerId) {

	}

	/**
	 * Gets the first player's deck as a list of strings.
	 * 
	 * @return a list of strings representing the deck.
	 */
	private static List<String> getFirstPlayerDeck() {
		LinkedList<String> firstPlayerCards = new LinkedList<String>();
		// starting hand will be 6 water elements, first card they draw will be
		// water spirit.
		firstPlayerCards.addLast("Water Element");
		firstPlayerCards.addLast("Water Element");
		firstPlayerCards.addLast("Water Element");
		firstPlayerCards.addLast("Water Element");
		firstPlayerCards.addLast("Water Element");
		firstPlayerCards.addLast("Water Element");
		firstPlayerCards.addLast("Water Spirit");

		// the next card the user will draw (start of 2nd turn) will be Delve The
		// Depths.
		firstPlayerCards.addLast("Delve The Depths");

		// now just give them a bunch of riptide cards.
		firstPlayerCards.addLast("Riptide");
		firstPlayerCards.addLast("Riptide");
		firstPlayerCards.addLast("Riptide");
		firstPlayerCards.addLast("Riptide");
		firstPlayerCards.addLast("Riptide");
		firstPlayerCards.addLast("Riptide");
		firstPlayerCards.addLast("Riptide");
		firstPlayerCards.addLast("Riptide");
		firstPlayerCards.addLast("Riptide");
		firstPlayerCards.addLast("Riptide");
		firstPlayerCards.addLast("Riptide");

		return firstPlayerCards;
	}

	/**
	 * Gets the second player's (not human) deck as a list of strings.
	 * 
	 * @return a list of strings representing the deck.
	 */
	private static List<String> getSecondPlayerDeck() {
		// our current implementation of the demo game allows us to give both
		// players the same decks.
		// this can obviously be swapped out as improvements to the tutorial system
		// are made!
		return getFirstPlayerDeck();
	}

}
