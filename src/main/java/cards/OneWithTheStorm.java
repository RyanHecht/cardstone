package cards;

import cardgamelibrary.AuraCard;
import cardgamelibrary.Board;
import cardgamelibrary.Card;
import cardgamelibrary.CardType;
import cardgamelibrary.Effect;
import cardgamelibrary.ManaPool;
import cardgamelibrary.Zone;
import devotions.AirDevotion;
import effects.CancelledEffect;
import effects.EffectType;
import effects.EmptyEffect;
import effects.PlayerDamageEffect;
import game.Player;

public class OneWithTheStorm extends AuraCard{

	private static final String defaultImage = "images/OneWithTheStorm.jpg";
	private static final String defaultName = "One with the storm";
	private static final String defaultText = "Aura. Your health is equal to your storm charge, and you do not take damage.";
	private static final CardType defaultType = CardType.AURA;

	public OneWithTheStorm(Player owner) {
		super(new ManaPool(15, 0, 0, 0, 3, 0), defaultImage, owner, defaultName, defaultText, defaultType);
	}
	
	public Effect onThisPlayed(Card c, Zone z){
		setPlayersHealth();
		return EmptyEffect.create();
	}
	
	public Effect onOtherCardPlayed(Card c, Zone z){
		if(z.equals(Zone.AURA_BOARD)){
			setPlayersHealth();
		}
		return EmptyEffect.create();
	}
	
	private void setPlayersHealth(){
		getOwner().setLife(AirDevotion.getLevelOfAir(getOwner().getDevotion()));
	}
	
	public boolean onProposedEffect(Effect e, Zone z, Board b){
		if(z.equals(Zone.AURA_BOARD)){
			if(e.getType().equals(EffectType.PLAYER_DAMAGED)){
				PlayerDamageEffect pde = (PlayerDamageEffect) e;
				if(pde.getPlayerDamaged().equals(getOwner())){
					return true;
				}
			}
		}
		return false;
	}
	
	public Effect getNewProposition(Effect e, Zone z){
		return new CancelledEffect(this);
	}
	
}
