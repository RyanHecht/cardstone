package cards;

import cardgamelibrary.Card;
import cardgamelibrary.CardType;
import cardgamelibrary.ConcatEffect;
import cardgamelibrary.Creature;
import cardgamelibrary.Effect;
import cardgamelibrary.ElementType;
import cardgamelibrary.ManaPool;
import cardgamelibrary.Zone;
import effects.EmptyEffect;
import effects.GiveElementEffect;
import effects.KillCreatureEffect;
import effects.SummonEffect;
import game.Player;

public class IlJunSkyChild extends Creature{

	  private static final String defaultImage = "images/IlJunSkyChild.jpg";
	  private static final String defaultName = "Il-Jun, Sky Child";
	  private static final String defaultText = "Whenever you play an air card, add one air to your pool. If you play a non-air element, destroy iljun and add a free longing for the sky to your hand.";
	  private static final int defaultHealth = 3;
	  private static final int defaultAttack = 3;
	  private static final CardType defaultType = CardType.CREATURE;

	  public IlJunSkyChild(Player owner) {
	    super(defaultHealth, defaultAttack, new ManaPool(60, 0, 0, 1, 0, 0),
	        defaultImage, owner, defaultName, defaultText, defaultType);
	  }
	  
	  public Effect onOtherCardPlayed(Card c, Zone z){
		  if(z.equals(Zone.CREATURE_BOARD) && c.getOwner().equals(getOwner())){
			  if(c.hasElement(ElementType.AIR)){
				  return new GiveElementEffect(getOwner(),ElementType.AIR,1,this);
			  }
		  }
		  if(z.equals(Zone.CREATURE_BOARD) && c.getType().equals(CardType.ELEMENT)){
			  if(!c.isA(AirElement.class)){
				  ConcatEffect ce = new ConcatEffect(this);
				  ce.addEffect(new KillCreatureEffect(this,this));
				  LongingForTheSky lfts = new LongingForTheSky(getOwner());
				  lfts.setCost(new ManaPool(0,0,0,0,0,0));
				  ce.addEffect(new SummonEffect(lfts,Zone.HAND,this));
				  return ce;
			  }
		  }
		  return EmptyEffect.create();
	  }
	
}
