package events;

import cardgamelibrary.ElementType;
import cardgamelibrary.Event;
import cardgamelibrary.EventType;
import game.Player;

public class GainElementEvent extends Event {

	private Player			player;
	private ElementType	type;
	private int					amount;

	public GainElementEvent(Player p, ElementType type, int amount) {
		super();
		this.player = p;
		this.type = type;
		this.amount = amount;
	}

	public Player getPlayer() {
		return player;
	}

	public int getAmount() {
		return amount;
	}

	public ElementType getElementType() {
		return type;
	}

	@Override
	public EventType getType() {
		return EventType.ELEMENT_GAINED;
	}
}
