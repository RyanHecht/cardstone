package templates;

import cardgamelibrary.Card;
import cardgamelibrary.Effect;
import cardgamelibrary.Zone;
import effects.EmptyEffect;
import game.Player;

public interface ActiveOneTurnCard extends Card{

	public void decrementValue();
	
	public int getValue();
	
	public void setValue(int value);
	
	public default Effect onTurnEnd(Player p, Zone z){
		decrementValue();
		if(isActive()){
			return activate();
		}
		return EmptyEffect.create();
	}
	
	public int getInitValue();
	
	public default Effect onThisPlayed(Card c, Zone z){
		setValue(getInitValue());
		return EmptyEffect.create();
	}
	
	public default boolean isActive(){
		return getValue() == 1;
	}
	
	public default Effect activate(){
		return EmptyEffect.create();
	}
	
}
