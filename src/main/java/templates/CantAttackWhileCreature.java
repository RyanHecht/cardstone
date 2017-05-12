package templates;

import cardgamelibrary.CreatureInterface;
import cardgamelibrary.Event;
import cardgamelibrary.EventType;
import cardgamelibrary.Zone;
import events.CreatureAttackEvent;
import events.PlayerAttackEvent;

public interface CantAttackWhileCreature extends CreatureInterface{

	
	
	boolean attackAllowedYet();
	
	public default boolean onProposedLegalityEvent(Event e, Zone z) {
	    if (!canAttack()) {
	      if (e.getType() == EventType.CREATURE_ATTACKED) {
	        CreatureAttackEvent eve = (CreatureAttackEvent) e;
	        return eve.getAttacker().equals(this);
	      } else if (e.getType() == EventType.PLAYER_ATTACKED) {
	        PlayerAttackEvent eve = (PlayerAttackEvent) e;
	        return eve.getAttacker().equals(this);
	      }
	    }
	    return false;
	  }

	  public default String getComplaint(Event e, Zone z) {
	    return "This creature may not attack";
	  }
	
}
