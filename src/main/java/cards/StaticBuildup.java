package cards;

import cardgamelibrary.Card;
import cardgamelibrary.CardType;
import cardgamelibrary.Effect;
import cardgamelibrary.ManaPool;
import cardgamelibrary.SpellCard;
import cardgamelibrary.Zone;
import effects.CardDamageEffect;
import effects.EffectType;
import effects.EmptyEffect;
import effects.PlayerDamageEffect;
import game.Player;

public class StaticBuildup extends SpellCard{

	private static final ManaPool defaultCost = new ManaPool(30, 0, 0, 0, 1, 0);
	private static final String defaultImage = "images/StaticBuildup.jpg";
	private static final String defaultName = "Static Buildup";
	private static final String defaultText = "On your next turn, every damaging spell does 1 more damage for each damaging spell you previously played that turn.";
	private static final CardType defaultType = CardType.SPELL;
	private int turnsLeft;
	private int buildup;

	public StaticBuildup(Player owner) {
		super(defaultCost, defaultImage, owner, defaultName, defaultText, defaultType);
		turnsLeft = 0;
		buildup = 0;
	}
	
	public Effect onThisPlayed(Card c, Zone z){
		turnsLeft = 3;
		return EmptyEffect.create();
	}
	

	public boolean onProposedEffect(Effect e, Zone z){
		if(turnsLeft == 1 && z == Zone.GRAVE){
			if(e.getType() == EffectType.PLAYER_DAMAGED && e.getSrc().getOwner() == getOwner()){
				return true;
			}
			if(e.getType() == EffectType.CARD_DAMAGED){
				return true;
			}
			if(e.getType() == EffectType.AOE_DAMAGE){
				return true;
			}
		}
		return false;
	}
	
	public Effect getNewProposition(Effect e, Zone z){
		if(e.getType() == EffectType.PLAYER_DAMAGED && e.getSrc().getOwner() == getOwner()){
			PlayerDamageEffect pde = (PlayerDamageEffect) e;
			PlayerDamageEffect newEff = new PlayerDamageEffect(pde.getPlayerDamaged(),pde.getSource(),pde.getDmg() + buildup);
			buildup++;
			return newEff;
		}
		if(e.getType() == EffectType.CARD_DAMAGED){
			CardDamageEffect pde = (CardDamageEffect) e;
			CardDamageEffect newEff = new CardDamageEffect(pde.getSrc(),pde.getTarget(),pde.getDmg() + buildup);
			buildup++;
			return newEff;
		}
		if(e.getType() == EffectType.AOE_DAMAGE){
			
		}
	}
	
}
