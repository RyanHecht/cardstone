package events;

import cardgamelibrary.CreatureInterface;
import cardgamelibrary.Event;
import cardgamelibrary.EventType;
import game.Player;

public class PlayerAttackEvent extends Event {
	private Player target;
	private CreatureInterface attacker;

	public PlayerAttackEvent(Player target, CreatureInterface attacker2) {
		super();
		this.target = target;
		this.attacker = attacker2;
	}

	public Player getTarget() {
		return target;
	}

	public CreatureInterface getAttacker() {
		return attacker;
	}

	@Override
	public EventType getType() {
		// TODO Auto-generated method stub
		return EventType.PLAYER_ATTACKED;
	}
}
