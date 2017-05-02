package events;

import cardgamelibrary.Event;
import cardgamelibrary.EventType;
import game.Player;

public class TurnEndEvent extends Event {

	private Player finished;

	public TurnEndEvent(Player p) {
		finished = p;
	}

	public Player getPlayer() {
		return finished;
	}

	@Override
	public EventType getType() {
		return EventType.TURN_END;
	}

}
