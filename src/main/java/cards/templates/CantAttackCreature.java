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
import game.Player;

public interface CantAttackCreature extends Card {

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
	
	default public String getComplaint(Event e, Zone z){
		return "This creature may not attack";
	}

	public default boolean getAllowedAttack(Creature attacker, Creature target){
		if(attacker == this){
			return true;
		}
		return false;
	}

	public default boolean getAllowedAttack(Creature attacker){
		if(attacker == this){
			return true;
		}
		return false;
	}
}
