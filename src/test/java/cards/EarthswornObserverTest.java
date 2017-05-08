package cards;

import cardgamelibrary.Board;
import cardgamelibrary.Zone;
import effects.CardDamageEffect;
import effects.SummonEffect;
import events.TurnEndEvent;
import game.Game;
import game.GameManager;
import game.Player;
import java.util.LinkedList;
import java.util.List;
import org.junit.Test;

public class EarthswornObserverTest {
  @Test
  public void testObserver() {
    List<String> firstPlayerDeck = new LinkedList<String>();
    firstPlayerDeck.add("Fire Element");
    firstPlayerDeck.add("Water Element");
    firstPlayerDeck.add("Earth Element");
    firstPlayerDeck.add("Air Element");
    firstPlayerDeck.add("Jun's Bolt");
    List<String> secondPlayerDeck = new LinkedList<String>();
    secondPlayerDeck.add("Fire Element");
    secondPlayerDeck.add("Water Element");
    secondPlayerDeck.add("Earth Element");
    secondPlayerDeck.add("Air Element");
    secondPlayerDeck.add("Jun's Bolt");

    // construct dummy game.
    Game g = new Game(firstPlayerDeck, secondPlayerDeck, -1, -2, false);

    Board b = g.getBoard();

    // add game to game manager
    GameManager.addGame(g);

    Player active = b.getActivePlayer();
    Player inactive = b.getInactivePlayer();

    CaveDwellingGoblin activeToDie = new CaveDwellingGoblin(active);
    CaveDwellingGoblin inactiveToDie = new CaveDwellingGoblin(inactive);
    EarthswornObserver toBeHealed = new EarthswornObserver(active);
    // summon all creatures.
    SummonEffect se1 = new SummonEffect(activeToDie, Zone.CREATURE_BOARD, null);
    SummonEffect se2 = new SummonEffect(inactiveToDie, Zone.CREATURE_BOARD,
        null);
    SummonEffect se3 = new SummonEffect(toBeHealed, Zone.CREATURE_BOARD, null);

    b.handleEffect(se1);
    b.handleEffect(se2);
    b.handleEffect(se3);

    CardDamageEffect dc1 = new CardDamageEffect(null, activeToDie, 3);
    CardDamageEffect dc2 = new CardDamageEffect(null, inactiveToDie, 3);

    b.handleEffect(dc1);

    b.takeAction(new TurnEndEvent(active));
    assert (toBeHealed.getHealth() == 10);

    b.handleEffect(dc2);
    b.takeAction(new TurnEndEvent(inactive));
    assert (toBeHealed.getHealth() == 10);
  }
}
