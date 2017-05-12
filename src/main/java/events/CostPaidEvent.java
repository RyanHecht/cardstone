package events;

import cardgamelibrary.Event;
import cardgamelibrary.EventType;
import cardgamelibrary.ManaPool;
import game.Player;

public class CostPaidEvent extends Event {

	private Player target;
	private ManaPool cost;
	

	
	public Player getTarget() {
		return target;
	}



	public ManaPool getCost() {
		return cost;
	}




	public CostPaidEvent(Player target, ManaPool cost) {
		super();
		this.target = target;
		this.cost = cost;
	}



	@Override
	public EventType getType() {
		return EventType.COST_PAID;
	}
	
}
