package events;

import cardgamelibrary.Card;
import cardgamelibrary.CreatureInterface;
import cardgamelibrary.Event;
import cardgamelibrary.EventType;

public class CardHealedEvent extends Event {

	private CreatureInterface target;
	private Card src;
	private int heal;

	public CardHealedEvent(CreatureInterface target2, Card src, int heal) {
		super();
		this.target = target2;
		this.src = src;
		this.heal = heal;
	}

	public CreatureInterface getTarget() {
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
		return EventType.CARD_HEALED;
	}
}
