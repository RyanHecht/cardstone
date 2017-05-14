package cards;

import cardgamelibrary.Board;
import cardgamelibrary.Card;
import cardgamelibrary.CardType;
import cardgamelibrary.Creature;
import cardgamelibrary.CreatureInterface;
import cardgamelibrary.Effect;
import cardgamelibrary.ManaPool;
import cardgamelibrary.Zone;
import devotions.AirDevotion;
import effects.AoeDamageEffect;
import effects.EmptyEffect;
import game.Player;
import lambda.FunctionThree;
import lambda.FunctionThreeMaker;

public class BubbleFairy extends Creature{

	private static final int			defaultMaxHealth	= 3;
	private static final int			defaultAttack		= 2;
	private static final String		defaultImage			= "images/BubbleFairy.jpg";
	private static final String		defaultName			= "Bubble Fairy";
	private static final String		defaultText			= "If you have at least 5 storm, deal 2 to all enemies";
	
	public BubbleFairy(Player owner) {
		super(defaultMaxHealth, defaultAttack, new ManaPool(20, 0, 0, 0, 2, 0), defaultImage, owner, defaultName, defaultText, CardType.CREATURE);
	}

	
	public Effect onThisPlayed(Card c, Zone z){
		if(AirDevotion.getLevelOfAir(getOwner().getDevotion()) >= 5){
			return new AoeDamageEffect(3,0,3,false,true,this,FunctionThreeMaker.targetsOtherPlayerCreatures(getOwner()),
					FunctionThreeMaker.determineCreatureDamage(0));
		}
		return EmptyEffect.create();
	}
	
}
