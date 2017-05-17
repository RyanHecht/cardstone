package events;

import cardgamelibrary.Event;
import cardgamelibrary.EventType;
import game.Player;

public class GainResourceEvent extends Event{

	private Player player;
	private int amount;

	public GainResourceEvent(Player player, int amount) {
		this.player = player;
		this.amount = amount;
	}
	
	public EventType getType(){
		return EventType.RESOURCE_GAINED;
	}

}
