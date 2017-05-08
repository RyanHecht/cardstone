package cards;

import cardgamelibrary.Card;
import cardgamelibrary.CardType;
import cardgamelibrary.CreatureInterface;
import cardgamelibrary.Effect;
import cardgamelibrary.ManaPool;
import cardgamelibrary.SpellCard;
import cardgamelibrary.Zone;
import effects.ApplyEffect;
import game.Player;
import templates.TargetsOtherCard;
import templates.decorators.CantAttackForTurnsCreature;

public class Sinkhole extends SpellCard implements TargetsOtherCard {

  private static final String defaultImage = "images/sinkhole.jpg";
  private static final String defaultName = "Sinkhole";
  private static final String defaultText = "Target creature cannot attack next turn.";
  private static final CardType defaultType = CardType.SPELL;

  public Sinkhole(Player owner) {
    super(new ManaPool(10, 0, 0, 1, 0, 0), defaultImage, owner, defaultName,
        defaultText, defaultType);
  }

  @Override
  public Effect impactCardTarget(Card target, Zone targetIn) {
    return new ApplyEffect(target,
        new CantAttackForTurnsCreature((CreatureInterface) target, 2), this);
  }

  @Override
  public boolean cardValidTarget(Card card, Zone targetIn) {
    if (card.getType() == CardType.CREATURE
        && targetIn == Zone.CREATURE_BOARD) {
      return true;
    } else {
      return false;
    }
  }

}
