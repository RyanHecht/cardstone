package cards;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

import com.google.gson.JsonObject;

import cardgamelibrary.Board;
import cardgamelibrary.Card;
import cardgamelibrary.ElementType;
import cardgamelibrary.OrderedCardCollection;
import cardgamelibrary.Zone;
import game.Game;
import game.GameManager;

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

		Board b = g.getBoard();

		// add game to game manager
		GameManager.addGame(g);

		int activeId = b.getActivePlayer().getId();
		OrderedCardCollection activePlayerHand = b.getOcc(b.getActivePlayer(), Zone.HAND);
		Integer fireId = null;
		Integer waterId = null;
		Integer earthId = null;
		Integer airId = null;
		Integer balId = null;
		for (Card c : activePlayerHand) {
			System.out.println("CARD NAME: " + c.getName());
			if (c.getName().equals("Fire Element")) {
				fireId = c.getId();
			} else if (c.getName().equals("Water Element")) {
				waterId = c.getId();
			} else if (c.getName().equals("Earth Element")) {
				earthId = c.getId();
			} else if (c.getName().equals("Air Element")) {
				airId = c.getId();
			} else if (c.getName().equals("Balance Element")) {
				balId = c.getId();
			}
		}
		// make sure that all these ids are not null b/c they should exist!
		assertNotNull(fireId);
		assertNotNull(waterId);
		assertNotNull(airId);
		assertNotNull(earthId);
		assertNotNull(balId);

		JsonObject firePlayed = new JsonObject();
		JsonObject waterPlayed = new JsonObject();
		JsonObject earthPlayed = new JsonObject();
		JsonObject airPlayed = new JsonObject();
		JsonObject balPlayed = new JsonObject();

		firePlayed.addProperty("IID1", fireId);
		waterPlayed.addProperty("IID1", waterId);
		earthPlayed.addProperty("IID1", earthId);
		airPlayed.addProperty("IID1", airId);
		balPlayed.addProperty("IID1", balId);

		g.handleCardPlayed(firePlayed, activeId);
		assertEquals(b.getActivePlayer().getElem(ElementType.FIRE), 3);

		g.handleCardPlayed(waterPlayed, activeId);
		assertEquals(b.getActivePlayer().getElem(ElementType.WATER), 3);

		g.handleCardPlayed(earthPlayed, activeId);
		assertEquals(b.getActivePlayer().getElem(ElementType.EARTH), 3);

		g.handleCardPlayed(airPlayed, activeId);
		assertEquals(b.getActivePlayer().getElem(ElementType.AIR), 3);

		g.handleCardPlayed(balPlayed, activeId);
		assertEquals(b.getActivePlayer().getElem(ElementType.BALANCE), 3);
	}
}
