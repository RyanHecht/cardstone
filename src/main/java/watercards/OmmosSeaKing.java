package watercards;

import cardgamelibrary.Card;
import cardgamelibrary.CardType;
import cardgamelibrary.Creature;
import cardgamelibrary.Effect;
import cardgamelibrary.ManaPool;
import cardgamelibrary.Zone;
import devotions.WaterDevotion;
import effects.CastEffect;
import effects.EmptyEffect;
import game.Player;

public class OmmosSeaKing extends Creature{

	private static final int			defaultMaxHealth	= 7;
	private static final int			defaultAttack		= 7;
	private static final String		defaultImage			= "images/OmmosSeaKing.png";
	private static final String		defaultName			= "Ommos, Sea King";
	private static final String		defaultText			= "Low tide: greed of the sea king. "
			+ "Rising tide: Wrath of the Sea King. High tide: Call of the Sea King."
			+ " Falling tide: Wisdom of the Sea King.";
	
	public OmmosSeaKing(Player owner) {
		super(defaultMaxHealth, defaultAttack, new ManaPool(120, 0, 4, 0, 0, 0), defaultImage, owner, defaultName, defaultText, CardType.CREATURE);
	}
	
	//Card src, Class<Card> toCast, Player owner
	public Effect onThisPlayed(Card c, Zone z){
		switch(WaterDevotion.getLevelOfWater(getOwner().getDevotion())){
		case LOW:
			return new CastEffect(this,GreedOfTheSeaKing.class,getOwner());
		case RISING:
			return new CastEffect(this,WrathOfTheSeaKing.class,getOwner());
		case HIGH:
			return new CastEffect(this,CallOfTheSeaKing.class,getOwner());
		case FALLING:
			return new CastEffect(this,WisdomOfTheSeaKing.class,getOwner());
		default:
			return EmptyEffect.create();
		}
	}
	
	
}
