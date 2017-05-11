package cards;

import cardgamelibrary.Card;
import cardgamelibrary.CardType;
import cardgamelibrary.Creature;
import cardgamelibrary.Effect;
import cardgamelibrary.ManaPool;
import cardgamelibrary.Zone;
import effects.ApplyEffect;
import game.Player;
import templates.decorators.TauntCreature;

public class Gargoyle extends Creature {
  private static final String defaultImage = "images/gargoyle.jpg";
  private static final String defaultName = "Gargoyle";
  private static final String defaultText = "Taunt.";
  private static final CardType defaultType = CardType.CREATURE;

  public Gargoyle(Player player) {
    super(5, 4, new ManaPool(30, 0, 0, 2, 0, 0), defaultImage, player,
        "Gargoyle", defaultText, defaultType);
  }

  @Override
  public Effect onThisPlayed(Card card, Zone zone) {
    return new ApplyEffect(new TauntCreature(this), this, this);
  }

}
