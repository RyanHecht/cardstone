package cards;

import cardgamelibrary.Card;
import cardgamelibrary.CardType;
import cardgamelibrary.Effect;
import cardgamelibrary.ManaPool;
import cardgamelibrary.SpellCard;
import cardgamelibrary.Zone;
import devotions.AirDevotion;
import effects.DamageInterface;
import effects.EmptyEffect;
import game.Player;
import templates.ActiveOneTurnCard;

public class ApproachingStorm extends SpellCard implements ActiveOneTurnCard{

	private static final String		defaultImage	= "images/ApproachingStorm.jpg";
	private static final String		defaultName		= "Approaching Storm";
	private static final String		defaultText		= "Next turn, your damaging spells do 1 additional damage.";
	private static final CardType	defaultType		= CardType.SPELL;
	private int value;
	
	public ApproachingStorm(Player owner) {
		super(new ManaPool(20, 0, 0, 0, 2, 0), defaultImage, owner, defaultName, defaultText, defaultType);
		value = 0;
	}
	
	
	public boolean onProposedEffect(Effect e, Zone z){
		if(isActive()){
			if(e instanceof DamageInterface){
				if(e.getSrc().getOwner().equals(getOwner())){
					return true;
				}
			}
		}
		return false;
	}
	
	public Effect getNewProposition(Effect e, Zone z){
		if(e instanceof DamageInterface){
			DamageInterface di = (DamageInterface) e;
			di.setDamage(di.getDamage() + 1);
			return di;
		}
		return e;
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
		return 3;
	}
	
}
