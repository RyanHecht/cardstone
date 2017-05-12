package events;

import cardgamelibrary.CreatureInterface;
import cardgamelibrary.EventType;
import game.Player;

public class PreliminaryPlayerAttackEvent extends PlayerAttackEvent{

	public PreliminaryPlayerAttackEvent(Player target, CreatureInterface attacker) {
		super(target, attacker);
		// TODO Auto-generated constructor stub
	}
	
	public EventType getType(){
		return EventType.PRELIMINARY_PLAYER_ATTACK;
	}

}
