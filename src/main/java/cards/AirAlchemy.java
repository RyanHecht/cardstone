package cards;

import cardgamelibrary.Card;
import cardgamelibrary.CardType;
import cardgamelibrary.ConcatEffect;
import cardgamelibrary.Effect;
import cardgamelibrary.ElementType;
import cardgamelibrary.ManaPool;
import cardgamelibrary.SpellCard;
import cardgamelibrary.Zone;
import devotions.AirDevotion;
import effects.GiveResourceEffect;
import game.Player;

public class AirAlchemy extends SpellCard{
	
	private static final String defaultImage = "images/AirAlchemy.jpg";
	private static final String defaultName = "Air Alchemy";
	private static final String defaultText = "Spend half your air to gain 6x that much gold.";
	private static final CardType defaultType = CardType.SPELL;

	public AirAlchemy(Player owner) {
		super(new ManaPool(0, 0, 0, 0, 1, 0), defaultImage, owner, defaultName, defaultText, defaultType);
	}
	
	public Effect onThisPlayed(Card c, Zone z){
		ConcatEffect ce = new ConcatEffect(this);
		int airLevel = getOwner().getElem(ElementType.AIR) / 2;
		getOwner().payCost(new ManaPool(0,0,0,0,airLevel,0));
		ce.addEffect(new GiveResourceEffect(getOwner(),6*airLevel,this));
		return ce;
	}

}
