package cards;

import cardgamelibrary.Card;
import cardgamelibrary.CardType;
import cardgamelibrary.Effect;
import cardgamelibrary.ManaPool;
import cardgamelibrary.SpellCard;
import cardgamelibrary.Zone;
import devotions.AirDevotion;
import effects.GiveResourceEffect;
import game.Player;

public class AGustOfGold extends SpellCard{

	private static final String defaultImage = "images/AGustOfGold.jpg";
	private static final String defaultName = "A Gust of Gold";
	private static final String defaultText = "Gain 10 gold + 5 for every storm you have.";
	private static final CardType defaultType = CardType.SPELL;

	public AGustOfGold(Player owner) {
		super(new ManaPool(0, 0, 0, 0, 3, 0), defaultImage, owner, defaultName, defaultText, defaultType);
	}
	
	public Effect onThisPlayed(Card c, Zone z){
		return new GiveResourceEffect(getOwner(),10 + 5*AirDevotion.getLevelOfAir(getOwner().getDevotion()),this);
	}
	
}
