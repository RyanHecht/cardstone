package cards;

import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

import cardgamelibrary.Board;
import game.Game;
import game.GameManager;
import game.Player;

public class CherryBombTest {

	@Test
	public void testBomb() {
		List<String> firstPlayerDeck = new LinkedList<String>();
		firstPlayerDeck.add("Fire Element");
		firstPlayerDeck.add("Water Element");
		firstPlayerDeck.add("Earth Element");
		firstPlayerDeck.add("Air Element");
		firstPlayerDeck.add("Jun's Bolt");
		firstPlayerDeck.add("Cherry Bomb");
		List<String> secondPlayerDeck = new LinkedList<String>();
		secondPlayerDeck.add("Fire Element");
		secondPlayerDeck.add("Water Element");
		secondPlayerDeck.add("Earth Element");
		secondPlayerDeck.add("Air Element");
		secondPlayerDeck.add("Jun's Bolt");
		secondPlayerDeck.add("Cherry Bomb");

		// construct dummy game.
		Game g = new Game(firstPlayerDeck, secondPlayerDeck, -1, -2, false);

		Board b = g.getBoard();

		// add game to game manager
		GameManager.addGame(g);

		Player active = b.getActivePlayer();

	}
}
