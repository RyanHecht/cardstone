package cards;

import cardgamelibrary.Card;
import cardgamelibrary.CardType;
import cardgamelibrary.Creature;
import cardgamelibrary.Effect;
import cardgamelibrary.ElementType;
import cardgamelibrary.ManaPool;
import cardgamelibrary.Zone;
import devotions.EarthDevotion;
import effects.EmptyEffect;
import effects.SummonEffect;
import game.Player;
import templates.decorators.TauntCreature;

public class StoneSinger extends Creature {

  private static final String defaultImage = "images/StoneSinger.jpg";
  private static final String defaultName = "Stone Singer";
  private static final String defaultText = "Whenever you play a card with earth in its cost, summon a (sleeping stone)/(sleeping stone) stone golem with taunt.";
  private static final int defaultHealth = 4;
  private static final int defaultAttack = 3;
  private static final CardType defaultType = CardType.CREATURE;

  public StoneSinger(Player owner) {
    super(defaultHealth, defaultAttack, new ManaPool(35, 0, 0, 2, 0, 0),
        defaultImage, owner, defaultName, defaultText, defaultType);
  }

  @Override
  public Effect onOtherCardPlayed(Card c, Zone z) {
    if (z == Zone.CREATURE_BOARD && c.getOwner() == getOwner()) {
      if (c.hasElement(ElementType.EARTH)) {
    	  StoneGolem sg = new StoneGolem(getOwner());
    	  sg.setStats(EarthDevotion.getLevelOfEarth(getOwner().getDevotion()));
        TauntCreature tc = new TauntCreature(sg);
        return new SummonEffect(tc, Zone.CREATURE_BOARD, this);
      }
    }
    return EmptyEffect.create();
  }

  private static class StoneGolem extends Creature {
    private static final ManaPool defaultCost = new ManaPool(10, 0, 0, 1, 0, 0);
    private static final String defaultImage = "images/StoneGolem.jpg";
    private static final String defaultName = "Stone Golem";
    private static final String defaultText = "Taunt.";
    private static final int defaultHealth = 2;
    private static final int defaultAttack = 1;
    private static final CardType defaultType = CardType.CREATURE;

    public StoneGolem(Player owner) {
      super(defaultHealth, defaultAttack, defaultCost, defaultImage, owner,
          defaultName, defaultText, defaultType);
    }
    
    private void setStats(int x){
    	this.setHealth(x);
    	this.setAttack(x);
    }
  }

}
