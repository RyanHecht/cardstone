package events;

import cardgamelibrary.Creature;
import cardgamelibrary.Event;
import cardgamelibrary.EventType;
import game.Player;

public class PlayerAttackEvent extends Event {
	private Player		target;
	private Creature	attacker;

	public PlayerAttackEvent(Player target, Creature attacker) {
		this.target = target;
		this.attacker = attacker;
	}

	public Player getTarget() {
		return target;
	}

	public Creature getAttacker() {
		return attacker;
	}

	@Override
	public EventType getType() {
		// TODO Auto-generated method stub
		return EventType.PLAYER_ATTACKED;
	}
}
