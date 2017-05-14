package game;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

import com.google.common.collect.ImmutableList;

import cardgamelibrary.Board;
import cardgamelibrary.Card;
import cardgamelibrary.Effect;
import cardgamelibrary.Zone;
import effects.PlayerDamageEffect;

public class GameManagerTest {
  private static final List<String> firstDeck = ImmutableList
      .of("Earth Element", "Forest Of Life", "Cheap Josh Creature");

  private static final List<String> secondDeck = ImmutableList.of("Air Element",
      "Juns Bolt", "Air Element");

  @Test
  public void testInsertAndSerialization() {
    Game g = new Game(firstDeck, secondDeck, 1, 2, false);
    Game gg = new Game(firstDeck, secondDeck, 3, 4, false);
    GameManager.addGame(g);
    GameManager.addGame(gg);

    Game p1g = GameManager.getGameByPlayerId(1);
    Game p2g = GameManager.getGameByPlayerId(2);
    
    Game p3g = GameManager.getGameByPlayerId(3);
    Game p4g = GameManager.getGameByPlayerId(4);

    System.out.println(new Integer(g.getId()) == null);
    System.out.println(new Integer(p1g.getId()) == null);
    assertEquals(g.getId(), p1g.getId());
    assertEquals(g.getId(), p2g.getId());

    assertEquals(g.getBoard().getAllCards(), p1g.getBoard().getAllCards());
    assertEquals(g.getBoard().getAllCards(), p2g.getBoard().getAllCards());

    assertEquals(gg.getBoard().getAllCards(), p4g.getBoard().getAllCards());
    assertEquals(p3g.getBoard().getAllCards(), p4g.getBoard().getAllCards());
    assertTrue(
        !g.getBoard().getAllCards().equals(p3g.getBoard().getAllCards()));

    GameManager.endGame(new GameStats(g, 0)); // end games as ties
    GameManager.endGame(new GameStats(gg, 0));
  }

  @Test
  public void testTurnEvents() {
    Game g = new Game(firstDeck, secondDeck, 1, 2, true);
    GameManager.addGame(g);

    Board b = g.getBoard();
    Player active = b.getActivePlayer();
    Card firstCard = b.getOcc(active, Zone.HAND).getFirstCard();
    Effect e = new PlayerDamageEffect(b.getInactivePlayer(), firstCard, 10);
    b.handleEffect(e);
    assertEquals(20, b.getInactivePlayer().getLife());

    g.sendWholeBoardToAllAndDb();

    Game g1 = GameManager.getGameByPlayerId(1);
    Game g2 = GameManager.getGameByPlayerId(2);

    System.out
        .println("Life is: " + g1.getBoard().getInactivePlayer().getLife());
    System.out
        .println("Life is: " + g2.getBoard().getInactivePlayer().getLife());

    assertEquals(20, g1.getBoard().getInactivePlayer().getLife());
    assertEquals(g2.getBoard().getInactivePlayer().getLife(),
        g1.getBoard().getInactivePlayer().getLife());

    GameManager.endGame(new GameStats(g, 0));
  }

}
