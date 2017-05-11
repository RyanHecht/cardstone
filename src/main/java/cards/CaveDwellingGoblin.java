package cards;

import cardgamelibrary.CardType;
import cardgamelibrary.Creature;
import cardgamelibrary.ManaPool;
import game.Player;

public class CaveDwellingGoblin extends Creature {

  private static final String defaultImage = "images/CaveDwellingGoblin.jpg";
  private static final String defaultName = "Cave Dwelling Goblin";
  private static final String defaultText = "";
  private static final int defaultHealth = 2;
  private static final int defaultAttack = 2;
  private static final CardType defaultType = CardType.CREATURE;

  public CaveDwellingGoblin(Player owner) {
    super(defaultHealth, defaultAttack, new ManaPool(10, 0, 0, 1, 0, 0),
        defaultImage, owner, defaultName, defaultText, defaultType);

  }

}
