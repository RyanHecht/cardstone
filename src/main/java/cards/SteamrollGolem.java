package cards;

import cardgamelibrary.Card;
import cardgamelibrary.CardType;
import cardgamelibrary.ConcatEffect;
import cardgamelibrary.Creature;
import cardgamelibrary.Effect;
import cardgamelibrary.ManaPool;
import cardgamelibrary.Zone;
import effects.CreatureAttackChangeEffect;
import effects.CreatureHealthChangeEffect;
import effects.EmptyEffect;
import effects.PayCostEffect;
import game.Player;
import templates.ActivatableCard;

public class SteamrollGolem extends Creature implements ActivatableCard{

	  private static final String defaultImage = "images/SteamrollGolem.jpg";
	  private static final String defaultName = "Steamroll Golem";
	  private static final String defaultText = "Activate for 10 resource: gain 1 attack this turn only and 1 health permanently.";
	  private static final int defaultHealth = 2;
	  private static final int defaultAttack = 1;
	  private static final CardType defaultType = CardType.CREATURE;
	  private int curTurnAttackBuff;

	  public SteamrollGolem(Player owner) {
	    super(defaultHealth, defaultAttack, new ManaPool(20, 0, 0, 2, 0, 0),
	        defaultImage, owner, defaultName, defaultText, defaultType);
	    curTurnAttackBuff = 0;
	  }

	  public Effect onThisPlayed(Card c, Zone z){
		  curTurnAttackBuff = 0;
		  return EmptyEffect.create();
	  }
	  
	  public Effect onTurnEnd(Player p, Zone z){
		  int buffRem = curTurnAttackBuff;
		  curTurnAttackBuff = 0;
		  return new CreatureAttackChangeEffect(buffRem * -1,this,this);
	  }
	  
	@Override
	public boolean canBeActivated(Zone z) {
		if(z.equals(Zone.CREATURE_BOARD)){
			return getOwner().validateCost(new ManaPool(10,0,0,0,0,0));
		}
		return false;
	}

	@Override
	public Effect onThisActivated() {
		ConcatEffect ce = new ConcatEffect(this);
		curTurnAttackBuff++;
		ce.addEffect(new CreatureAttackChangeEffect(1,this,this));
		ce.addEffect(new CreatureHealthChangeEffect(1,this,this));
		ce.addEffect(new PayCostEffect(this,new ManaPool(10,0,0,0,0,0),getOwner()));
		return ce;
	}
	  
	  
	
}
