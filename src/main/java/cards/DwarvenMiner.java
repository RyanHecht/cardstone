package cards;

import java.util.Iterator;

import com.google.common.base.Objects;

import cardgamelibrary.CardType;
import cardgamelibrary.ConcatEffect;
import cardgamelibrary.Creature;
import cardgamelibrary.Effect;
import cardgamelibrary.ManaPool;
import cardgamelibrary.Zone;
import effects.AddToOccEffect;
import effects.EffectType;
import effects.EmptyEffect;
import effects.PlayerDamageEffect;
import effects.PlayerHealedEffect;
import game.Player;

public class DwarvenMiner extends Creature{

	private static final ManaPool	defaultCost		= new ManaPool(40, 0, 0, 2, 0, 0);
	private static final String		defaultImage	= "images/DwarvenMiner.jpg";
	private static final String		defaultName		= "Dwarven Miner";
	private static final String		defaultText		= "Double the effect of all your auras.";
	private static final int			defaultHealth	= 4;
	private static final int			defaultAttack	= 3;
	private static final CardType	defaultType		= CardType.CREATURE;

	public DwarvenMiner(Player owner) {
		super(defaultHealth, defaultAttack, defaultCost, defaultImage, owner, defaultName, defaultText, defaultType);
	}
	
	public boolean onProposedEffect(Effect e, Zone z) {	
		if(z == Zone.CREATURE_BOARD && e.getSrc().getType() == CardType.AURA && e.getSrc().getOwner() == getOwner()){
			if(e.getType() == EffectType.CONCAT){
				ConcatEffect ce = (ConcatEffect) e;
				Iterator<Effect> it = ce.getEffects();
				while(it.hasNext()){
					Effect itted = it.next();
					if(itted.getType() == EffectType.CARD_ZONE_CHANGED){
						AddToOccEffect actoe = (AddToOccEffect) itted;
						if(actoe.getCard().equals(ce.getSrc())){
							if(actoe.getEnd() == Zone.AURA_BOARD && actoe.getStart() == Zone.HAND){
								return false;
							}
						}
					}
				}
			}
			if(e.getType() == EffectType.CARD_ZONE_CHANGED){
				AddToOccEffect actoe = (AddToOccEffect) e;
				if(actoe.getCard().equals(e.getSrc())){
					if(actoe.getEnd() == Zone.AURA_BOARD && actoe.getStart() == Zone.HAND){
						return false;
					}
				}
			}
			return true;
		}
		
		return false;
	}

	public Effect getNewProposition(Effect e, Zone z) {

		ConcatEffect ce = new ConcatEffect(e.getSrc());
		ce.addEffect(e);
		ce.addEffect(e);
		return ce;
	}
	
}
