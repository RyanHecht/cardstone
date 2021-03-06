package cards;

import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

import cardgamelibrary.Board;
import cardgamelibrary.CreatureInterface;
import cardgamelibrary.Zone;
import effects.CardDamageEffect;
import effects.SummonEffect;
import events.TurnEndEvent;
import game.Game;
import game.GameManager;
import game.Player;

public class WaterSpiritTest {

	@Test
	public void testBomb() {
		List<String> firstPlayerDeck = new LinkedList<String>();
		firstPlayerDeck.add("Fire Element");
		firstPlayerDeck.add("Water Element");
		firstPlayerDeck.add("Earth Element");
		firstPlayerDeck.add("Air Element");
		firstPlayerDeck.add("Jun's Bolt");
		firstPlayerDeck.add("Water Spirit");
		List<String> secondPlayerDeck = new LinkedList<String>();
		secondPlayerDeck.add("Fire Element");
		secondPlayerDeck.add("Water Element");
		secondPlayerDeck.add("Earth Element");
		secondPlayerDeck.add("Air Element");
		secondPlayerDeck.add("Jun's Bolt");
		secondPlayerDeck.add("Water Spirit");

		// construct dummy game.
		Game g = new Game(firstPlayerDeck, secondPlayerDeck, -1, -2, false);

		Board b = g.getBoard();

		// add game to game manager
		GameManager.addGame(g);

		Player active = b.getActivePlayer();
		Player inactive = b.getInactivePlayer();
		WaterSpirit w1 = new WaterSpirit(active);
		WaterSpirit w2 = new WaterSpirit(inactive);
		SummonEffect se1 = new SummonEffect(w1, Zone.CREATURE_BOARD, null);
		SummonEffect se2 = new SummonEffect(w2, Zone.CREATURE_BOARD, null);

		b.handleEffect(se1);
		assert (b.getOcc(active, Zone.CREATURE_BOARD).size() == 1);

		b.handleEffect(se2);
		assert (b.getOcc(b.getInactivePlayer(), Zone.CREATURE_BOARD).size() == 1);

		CardDamageEffect dc1 = new CardDamageEffect(null, (CreatureInterface) w1, 3);

		b.handleEffect(dc1);

		b.takeAction(new TurnEndEvent(active));
		assert (b.getOcc(active, Zone.CREATURE_BOARD).size() == 0);

	}
}
