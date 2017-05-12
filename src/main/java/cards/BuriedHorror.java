package cards;

import cardgamelibrary.CardType;
import cardgamelibrary.Creature;
import cardgamelibrary.CreatureInterface;
import cardgamelibrary.Effect;
import cardgamelibrary.Event;
import cardgamelibrary.EventType;
import cardgamelibrary.ManaPool;
import cardgamelibrary.Zone;
import effects.CreatureAttackCreatureEffect;
import effects.CreatureAttackPlayerEffect;
import effects.EffectType;
import effects.EmptyEffect;
import effects.KillCreatureEffect;
import events.CreatureAttackEvent;
import events.PlayerAttackEvent;
import game.Player;
import templates.CantAttackWhileCreature;

public class BuriedHorror extends Creature implements CantAttackWhileCreature{

	private static final String		defaultImage	= "images/BuriedHorror.jpg";
	private static final String		defaultName		= "Buried Horror";
	private static final String		defaultText		= "Can't attack. Next time an enemy minion attacks, instead destroy it and this can attack.";
	private static final int			defaultHealth	= 9;
	private static final int			defaultAttack	= 6;
	private static final CardType	defaultType		= CardType.CREATURE;
	private boolean buried;


	public BuriedHorror(Player owner) {
		super(defaultHealth, defaultAttack, new ManaPool(70, 0, 0, 2, 0, 0), defaultImage, owner, defaultName, defaultText, defaultType);
		this.buried = true;
	}
	  
	  public boolean onProposedEffect(Effect e, Zone z){
		  if(buried && z.equals(Zone.CREATURE_BOARD)){
			  if (e.getType() == EffectType.CREATURE_ATTACK_CREATURE) {
		        CreatureAttackCreatureEffect eve = (CreatureAttackCreatureEffect) e;
		        return !eve.getAttacker().getOwner().equals(getOwner());
		      } else if (e.getType() == EffectType.PLAYER_ATTACKED) {
		        CreatureAttackPlayerEffect eve = (CreatureAttackPlayerEffect) e;
		        return !eve.getAttacker().getOwner().equals(getOwner());
		      }
		  }
		return false;
	  }
	  
	  public Effect getNewProposition(Effect e, Zone z){
		  
		  if(buried){
			  if (e.getType() == EffectType.CREATURE_ATTACK_CREATURE) {
			        CreatureAttackCreatureEffect eve = (CreatureAttackCreatureEffect) e;
			        return new KillCreatureEffect(eve.getAttacker(),this);
			      } else if (e.getType() == EffectType.PLAYER_ATTACKED) {
			        CreatureAttackPlayerEffect eve = (CreatureAttackPlayerEffect) e;
			        return new KillCreatureEffect(eve.getAttacker(),this);
			      }
		  }
		  return e;
	  }
	  
	  public Effect onCreatureAttacked(CreatureInterface attacker, CreatureInterface target, Zone z){
		  if(buried){
			  if(attacker.getOwner().equals(getOwner())){
				  buried = false;
				  return new KillCreatureEffect(attacker,this);
			  }
		  }
		  return EmptyEffect.create();
	  }

	@Override
	public boolean attackAllowedYet() {
		return !buried;
	}
}
