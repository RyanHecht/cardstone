package cards;

import cardgamelibrary.Board;
import cardgamelibrary.Zone;
import effects.AddToOccEffect;
import effects.SummonEffect;
import game.Game;
import game.GameManager;
import game.Player;
import java.util.LinkedList;
import java.util.List;
import org.junit.Test;

public class ForestOfLifeTest {
  @Test
  public void test() {
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

    ForestOfLife activeHealer = new ForestOfLife(active);
    CaveDwellingGoblin activeJoshCr = new CaveDwellingGoblin(active);
    CaveDwellingGoblin inactiveJoshCr = new CaveDwellingGoblin(inactive);
    EarthswornObserver toBeHealed = new EarthswornObserver(active);

    // play the aura.
    SummonEffect auraSummon = new SummonEffect(activeHealer, Zone.AURA_BOARD,
        null);

    // summon all creatures.
    SummonEffect se1 = new SummonEffect(activeJoshCr, Zone.HAND, null);
    SummonEffect se2 = new SummonEffect(inactiveJoshCr, Zone.HAND, null);
    SummonEffect se3 = new SummonEffect(toBeHealed, Zone.HAND, null);

    AddToOccEffect ae1 = new AddToOccEffect(activeJoshCr, active,
        Zone.CREATURE_BOARD, Zone.HAND, null);
    AddToOccEffect ae2 = new AddToOccEffect(inactiveJoshCr, active,
        Zone.CREATURE_BOARD, Zone.HAND, null);
    AddToOccEffect ae3 = new AddToOccEffect(toBeHealed, active,
        Zone.CREATURE_BOARD, Zone.HAND, null);

    // play aura to board.
    b.handleEffect(auraSummon);

    // add creatures to hand.
    b.handleEffect(se1);
    b.handleEffect(se2);
    b.handleEffect(se3);

    // play all creatures.
    b.handleEffect(ae1);
    b.handleEffect(ae2);
    b.handleEffect(ae3);

    System.out.println(
        "FOREST OF LIFE PLAYER HAS THIS MUCH HEALTH: " + active.getLife());
    // assert (active.getLife() == 36);
  }
}
