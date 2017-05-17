package watercards;

import cardgamelibrary.Card;
import cardgamelibrary.CardType;
import cardgamelibrary.Effect;
import cardgamelibrary.ManaPool;
import cardgamelibrary.SpellCard;
import cardgamelibrary.Zone;
import devotions.LockedWaterDevotion;
import devotions.TideLevel;
import devotions.WaterDevotion;
import effects.EmptyEffect;
import game.Player;
import templates.ActiveOneTurnCard;

public class PondEcosystem extends SpellCard implements ActiveOneTurnCard{

	private static final String defaultImage = "images/PondEcosystem.jpg";
	private static final String defaultName = "Pond Ecosystem";
	private static final String defaultText = "Your tide level remains at low tide for 5 turns.";
	private static final CardType defaultType = CardType.SPELL;
	private int value;
	private WaterDevotion wd;
	public PondEcosystem(Player owner) {
		super(new ManaPool(15, 0, 2, 0, 0, 0), defaultImage, owner, defaultName, defaultText, defaultType);
		value = 0;
	}
	@Override
	public void decrementValue() {
		value--;
	}
	@Override
	public int getValue() {
		return value;
	}
	@Override
	public void setValue(int value) {
		this.value = value;
	}
	@Override
	public int getInitValue() {
		return 10;
	}
	
	public Effect onThisPlayed(Card c, Zone z){
		if(!WaterDevotion.getLevelOfWater(getOwner().getDevotion()).equals(TideLevel.NOT_TIDAL)){
			this.wd = (WaterDevotion) getOwner().getDevotion();
			getOwner().setDevotion(new LockedWaterDevotion(getOwner()));
		}
		return EmptyEffect.create();
	}
	
	public Effect activate(){
		getOwner().setDevotion(new WaterDevotion(getOwner()));
		return EmptyEffect.create();
	}
	
}
