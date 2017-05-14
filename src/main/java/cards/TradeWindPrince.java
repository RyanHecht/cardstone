package cards;

import cardgamelibrary.Card;
import cardgamelibrary.CardType;
import cardgamelibrary.Creature;
import cardgamelibrary.Effect;
import cardgamelibrary.ElementType;
import cardgamelibrary.ManaPool;
import cardgamelibrary.Zone;
import devotions.AirDevotion;
import effects.GiveElementEffect;
import game.Player;

public class TradeWindPrince extends Creature{

	
	private static final String defaultImage = "images/TradeWindPrince.jpg";
	private static final String defaultName = "Trade Wind Prince";
	private static final String defaultText = "On play, for every storm you have, add 1 air to your pool.";
	private static final CardType defaultType = CardType.CREATURE;
	private static int defaultMaxHealth = 4;
	private static int defaultAttack = 5;

	public TradeWindPrince(Player owner) {
		super(defaultMaxHealth, defaultAttack, new ManaPool(50, 0, 0, 0, 1, 0), defaultImage, owner, defaultName, defaultText, CardType.CREATURE);
	}
	
	public Effect onThisPlayed(Card c, Zone z){
		return new GiveElementEffect(getOwner(),cardgamelibrary.ElementType.AIR,
				AirDevotion.getLevelOfAir(getOwner().getDevotion()),this);
	}

}
