package cards;

import cardgamelibrary.Card;
import cardgamelibrary.CardType;
import cardgamelibrary.Effect;
import cardgamelibrary.ManaPool;
import cardgamelibrary.SpellCard;
import cardgamelibrary.Zone;
import effects.CardDamageEffect;
import effects.DamageInterface;
import effects.EffectType;
import effects.EmptyEffect;
import effects.PlayerDamageEffect;
import game.Player;

public class StaticBuildup extends SpellCard{

	private static final String defaultImage = "images/StaticBuildup.jpg";
	private static final String defaultName = "Static Buildup";
	private static final String defaultText = "For the rest of the turn, every damaging spell does 2 more damage for each spell you previously played that turn.";
	private static final CardType defaultType = CardType.SPELL;
	private int turnsLeft;
	private int buildup;

	public StaticBuildup(Player owner) {
		super(new ManaPool(35, 0, 0, 0, 1, 0), defaultImage, owner, defaultName, defaultText, defaultType);
		turnsLeft = 0;
		buildup = 0;
	}
	
	public Effect onThisPlayed(Card c, Zone z){
		turnsLeft = 1;
		return EmptyEffect.create();
	}
	
	public Effect onTurnStart(Player p, Zone z){
		turnsLeft--;
		return EmptyEffect.create();
	}
	
	public Effect onOtherCardPlayed(Card c, Zone z){
		if(c.getType() == CardType.SPELL && turnsLeft == 1){
			buildup+=2;
		}
		return EmptyEffect.create();
	}
	
	public boolean onProposedEffect(Effect e, Zone z){
		if(turnsLeft == 1 && z == Zone.GRAVE){
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
			di.setDamage(di.getDamage() + buildup);
			return di;
		}
		return e;
	}
	
}
