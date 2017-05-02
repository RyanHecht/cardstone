package events;

import cardgamelibrary.Event;
import cardgamelibrary.EventType;
import game.Player;

public class TurnStartEvent extends Event {

	// the player whose turn is beginning.
	private Player starting;

	public TurnStartEvent(Player p) {
		starting = p;
	}

	public Player getPlayer() {
		return starting;
	}

	@Override
	public EventType getType() {
		// TODO Auto-generated method stub
		return EventType.TURN_START;
	}

}
