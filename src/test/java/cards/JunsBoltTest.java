package cards;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

import com.google.gson.JsonObject;

import cardgamelibrary.Board;
import cardgamelibrary.Card;
import cardgamelibrary.OrderedCardCollection;
import cardgamelibrary.Zone;
import game.Game;
import game.GameManager;
import game.Player;

public class JunsBoltTest {
	@Test
	public void testOnPlayer() {
		// should test targeting both a player and a creature here.

		List<String> firstPlayerDeck = new LinkedList<String>();
		firstPlayerDeck.add("Fire Element");
		firstPlayerDeck.add("Water Element");
		firstPlayerDeck.add("Earth Element");
		firstPlayerDeck.add("Air Element");
		firstPlayerDeck.add("Balance Element");
		firstPlayerDeck.add("Jun's Bolt");
		List<String> secondPlayerDeck = new LinkedList<String>();
		secondPlayerDeck.add("Fire Element");
		secondPlayerDeck.add("Water Element");
		secondPlayerDeck.add("Earth Element");
		secondPlayerDeck.add("Air Element");
		secondPlayerDeck.add("Balance Element");
		secondPlayerDeck.add("Jun's Bolt");

		// construct dummy game.
		Game g = new Game(firstPlayerDeck, secondPlayerDeck, -1, -2, false);

		Board b = g.getBoard();

		// add game to game manager
		GameManager.addGame(g);

		Player activePlayer = b.getActivePlayer();
		Player inactivePlayer = b.getInactivePlayer();

		int activeId = activePlayer.getId();
		int inactiveId = b.getInactivePlayer().getId();

		JsonObject activeTurnEnd = new JsonObject();
		JsonObject inactiveTurnEnd = new JsonObject();
		JsonObject activeAirPlayed = new JsonObject();
		JsonObject activeBoltOnPlayer = new JsonObject();

		OrderedCardCollection activePlayerHand = b.getOcc(b.getActivePlayer(), Zone.HAND);
		Integer boltId = null;
		Integer airId = null;
		for (Card c : activePlayerHand) {
			System.out.println("CARD NAME: " + c.getName());
			if (c.getName().equals("air")) {
				airId = c.getId();
			} else if (c.getName().equals("Jun's Bolt")) {
				boltId = c.getId();
			}
		}
		assertNotNull(boltId);
		assertNotNull(airId);

		// set up json objects for user input.
		activeAirPlayed.addProperty("IID1", airId);
		activeBoltOnPlayer.addProperty("IID1", boltId);
		activeBoltOnPlayer.addProperty("self", false);

		// each player passes once.
		g.handleTurnend(activeTurnEnd, activeId);
		g.handleTurnend(inactiveTurnEnd, inactiveId);

		// assert that both players still have 30 life.
		assertEquals(activePlayer.getLife(), 30);
		assertEquals(inactivePlayer.getLife(), 30);

		// player plays the air element and the juns bolt.
		g.handleCardPlayed(activeAirPlayed, activeId);
		g.handlePlayerTargeted(activeBoltOnPlayer, activeId);

		// check to see if life totals are updated.
		assertEquals(activePlayer.getLife(), 30);
		assertEquals(inactivePlayer.getLife(), 27);
	}

	@Test
	public void testOnCard() {
		// should test targeting both a player and a creature here.

		List<String> firstPlayerDeck = new LinkedList<String>();
		firstPlayerDeck.add("Water Spirit");
		firstPlayerDeck.add("Water Element");
		firstPlayerDeck.add("Earth Element");
		firstPlayerDeck.add("Air Element");
		firstPlayerDeck.add("Balance Element");
		firstPlayerDeck.add("Jun's Bolt");
		List<String> secondPlayerDeck = new LinkedList<String>();
		secondPlayerDeck.add("Water Spirit");
		secondPlayerDeck.add("Water Element");
		secondPlayerDeck.add("Earth Element");
		secondPlayerDeck.add("Air Element");
		secondPlayerDeck.add("Balance Element");
		secondPlayerDeck.add("Jun's Bolt");

		// construct dummy game.
		Game g = new Game(firstPlayerDeck, secondPlayerDeck, -1, -2, false);

		Board b = g.getBoard();

		// add game to game manager
		GameManager.addGame(g);

		// add game to game manager
		GameManager.addGame(g);

		Player activePlayer = b.getActivePlayer();
		Player inactivePlayer = b.getInactivePlayer();

		int activeId = activePlayer.getId();
		int inactiveId = b.getInactivePlayer().getId();

		JsonObject activeTurnEnd = new JsonObject();
		JsonObject inactiveTurnEnd = new JsonObject();
		JsonObject activeAirPlayed = new JsonObject();
		JsonObject activeBoltOnCard = new JsonObject();
		JsonObject inactiveWaterPlayed = new JsonObject();
		JsonObject inactiveSpiritPlayed = new JsonObject();

		OrderedCardCollection activePlayerHand = b.getOcc(b.getActivePlayer(), Zone.HAND);
		OrderedCardCollection inactivePlayerHand = b.getOcc(b.getInactivePlayer(), Zone.HAND);

		Integer boltId = null;
		Integer airId = null;
		Integer waterId = null;
		Integer waterSpiritId = null;
		for (Card c : activePlayerHand) {
			System.out.println("CARD NAME: " + c.getName());
			if (c.getName().equals("air")) {
				airId = c.getId();
			} else if (c.getName().equals("Jun's Bolt")) {
				boltId = c.getId();
			}
		}

		for (Card c : inactivePlayerHand) {
			System.out.println("CARD NAME: " + c.getName());
			if (c.getName().equals("water")) {
				waterId = c.getId();
			} else if (c.getName().equals("Water Spirit")) {
				waterSpiritId = c.getId();
			}
		}
		assertNotNull(boltId);
		assertNotNull(airId);
		assertNotNull(waterId);
		assertNotNull(waterSpiritId);

		// set up json objects for user input.
		activeAirPlayed.addProperty("IID1", airId);
		activeBoltOnCard.addProperty("IID1", boltId);
		activeBoltOnCard.addProperty("IID2", waterSpiritId);

		inactiveWaterPlayed.addProperty("IID1", waterId);
		inactiveSpiritPlayed.addProperty("IID1", waterSpiritId);

		// first player passes once.
		g.handleTurnend(activeTurnEnd, activeId);

		// second player plays their element and creature, then ends their turn.
		g.handleCardPlayed(inactiveWaterPlayed, inactiveId);
		g.handleCardPlayed(inactiveSpiritPlayed, inactiveId);
		g.handleTurnend(inactiveTurnEnd, inactiveId);

		// assert that both players still have 30 life.
		assertEquals(activePlayer.getLife(), 30);
		assertEquals(inactivePlayer.getLife(), 30);

		// player plays the air element and the juns bolt.
		g.handleCardPlayed(activeAirPlayed, activeId);
		g.handleCardTargeted(activeBoltOnCard, activeId);

		// check to see if life totals are unchanged.
		assertEquals(activePlayer.getLife(), 30);
		assertEquals(inactivePlayer.getLife(), 30);

		// check that the spirit is no longer on the board.
		assertEquals(b.getOcc(inactivePlayer, Zone.CREATURE_BOARD).size(), 0);
	}
}
