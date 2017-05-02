package events;

import cardgamelibrary.Card;
import cardgamelibrary.Event;
import cardgamelibrary.EventType;
import game.Player;

public class PlayerHealedEvent extends Event {

	private Player	target;
	private Card		src;
	private int			heal;

	public PlayerHealedEvent(Player target, Card src, int heal) {
		super();
		this.target = target;
		this.src = src;
		this.heal = heal;
	}

	public Player getTarget() {
		return target;
	}

	public Card getSrc() {
		return src;
	}

	public int getHeal() {
		return heal;
	}

	@Override
	public EventType getType() {
		return EventType.PLAYER_HEALED;
	}
}
