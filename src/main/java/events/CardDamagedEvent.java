package events;

import cardgamelibrary.Card;
import cardgamelibrary.CreatureInterface;
import cardgamelibrary.Event;
import cardgamelibrary.EventType;

public class CardDamagedEvent extends Event {

	private CreatureInterface target;
	private Card src;
	private int dmg;

	public CardDamagedEvent(CreatureInterface target2, Card src, int dmg) {
		super();
		this.target = target2;
		this.src = src;
		this.dmg = dmg;
	}

	public CreatureInterface getTarget() {
		return target;
	}

	public Card getSrc() {
		return src;
	}

	public int getDmg() {
		return dmg;
	}

	@Override
	public EventType getType() {
		// TODO Auto-generated method stub
		return EventType.CARD_DAMAGED;
	}

}
