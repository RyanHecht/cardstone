package cards;

import cardgamelibrary.Board;
import cardgamelibrary.Card;
import cardgamelibrary.CardType;
import cardgamelibrary.ConcatEffect;
import cardgamelibrary.Creature;
import cardgamelibrary.Effect;
import cardgamelibrary.ManaPool;
import cardgamelibrary.Zone;
import devotions.AirDevotion;
import effects.EffectMaker;
import effects.PlayerDamageEffect;
import effects.RandomCreatureDamage;
import game.Player;

public class StormDragon extends Creature{

	  private static final String defaultImage = "images/StormDragon.jpg";
	  private static final String defaultName = "Storm Dragon";
	  private static final String defaultText = "Deal 10 to the enemy player, and for every storm you have deal 1 to a random enemy.";
	  private static final int defaultHealth = 8;
	  private static final int defaultAttack = 12;
	  private static final CardType defaultType = CardType.CREATURE;

	  public StormDragon(Player owner) {
	    super(defaultHealth, defaultAttack, new ManaPool(150, 0, 0, 4, 0, 0),
	        defaultImage, owner, defaultName, defaultText, defaultType);
	  }
	  
	  public Effect onThisPlayed(Card c, Zone z){
		  ConcatEffect ce = new ConcatEffect(this);
		  ce.addEffect(new EffectMaker((Board b) -> {
			  return new PlayerDamageEffect(b.getOpposingPlayer(getOwner()),this,10);
		  },this));
		  int devLevel = AirDevotion.getLevelOfAir(getOwner().getDevotion());
		  for(int x = 0; x < devLevel; x++){
			  ce.addEffect(new RandomCreatureDamage(false,true,1,this));
		  }
		  return ce;
	  }
}
