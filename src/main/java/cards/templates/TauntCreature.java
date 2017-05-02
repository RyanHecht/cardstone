package cards.templates;

import cardgamelibrary.Card;
import cardgamelibrary.Creature;
import cardgamelibrary.Effect;
import cardgamelibrary.Event;
import cardgamelibrary.EventType;
import cardgamelibrary.Zone;
import effects.EmptyEffect;
import events.CreatureAttackEvent;
import events.PlayerAttackEvent;

public interface TauntCreature extends Card{

	default public boolean onProposedLegalityEvent(Event e, Zone z){
		if(e.getType() == EventType.CREATURE_ATTACKED){
			CreatureAttackEvent eve = (CreatureAttackEvent)e;
			return getAllowedAttack(eve.getAttacker(),eve.getTarget());
		}
		else if(e.getType() == EventType.PLAYER_ATTACKED){
			PlayerAttackEvent eve = (PlayerAttackEvent)e;
			return getAllowedAttack(eve.getAttacker());
		}
		return false;
	}
	
	default public boolean getAllowedAttack(Creature attacker){
		if(attacker.getOwner() == this.getOwner()){
			return false;
		}
		else{
			return false;
		}
	}

	default public boolean getAllowedAttack(Creature attacker, Creature target){
		if(attacker.getOwner() == this.getOwner()){
			return false;
		}
		else{
			if(target instanceof TauntCreature){
				return true;
			}
			return false;
		}
	}

	default public String getComplaint(Event e, Zone z){
		if(onProposedLegalityEvent(e,z)){
			return "Minions with taunt must be attacked first!";
		}
		return "Invalid Event";
	}
}
