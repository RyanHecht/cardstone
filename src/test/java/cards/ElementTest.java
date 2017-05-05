package cards;

import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

import game.Game;

public class ElementTest {

	@Test
	public void testElement() {
		List<String> firstPlayerDeck = new LinkedList<String>();
		firstPlayerDeck.add("Fire Element");
		firstPlayerDeck.add("Water Element");
		firstPlayerDeck.add("Earth Element");
		firstPlayerDeck.add("Air Element");
		firstPlayerDeck.add("Balance Element");
		List<String> secondPlayerDeck = new LinkedList<String>();
		secondPlayerDeck.add("Fire Element");
		secondPlayerDeck.add("Water Element");
		secondPlayerDeck.add("Earth Element");
		secondPlayerDeck.add("Air Element");
		secondPlayerDeck.add("Balance Element");

		// construct dummy game.
		Game g = new Game(firstPlayerDeck, secondPlayerDeck, -1, -2, false);

		// figure out turn order.
		int firstId = g.getActivePlayerId();
		int secondId;
		if (firstId == -1) {
			secondId = -2;
		} else {
			secondId = -1;
		}

	}
}
