package cards;

import cardgamelibrary.Card;
import cardgamelibrary.CardType;
import cardgamelibrary.Creature;
import cardgamelibrary.Effect;
import cardgamelibrary.ManaPool;
import cardgamelibrary.SpellCard;
import cardgamelibrary.Zone;
import effects.CardDamageEffect;
import game.Player;
import templates.TargetsOtherCard;

public class Riptide extends SpellCard implements TargetsOtherCard {

  private static final long serialVersionUID = 1L;
  private static final ManaPool defaultCost = new ManaPool(5, 0, 3, 0, 0, 0);
  private static final String defaultImage = "images/Riptide.jpg";
  private static final String defaultName = "Riptide";
  private static final String defaultText = "Deal two damage to target creature.";
  private static final CardType defaultType = CardType.SPELL;

  public Riptide(Player owner) {
    super(defaultCost, defaultImage, owner, defaultName, defaultText,
        defaultType);
  }

  @Override
  public boolean cardValidTarget(Card card, Zone targetIn) {
    if (card instanceof Creature && !(card.getOwner().equals(this.getOwner()))
        && targetIn == Zone.CREATURE_BOARD) {
      // target must be creature on board that belongs to opponent.
      return true;
    }
    return false;
  }

  @Override
  public Effect impactCardTarget(Card target, Zone targetIn) {
    assert (target instanceof Creature);
    return new CardDamageEffect(this, (Creature) target, 2);
  }

}
