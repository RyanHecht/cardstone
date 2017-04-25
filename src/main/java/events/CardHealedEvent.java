package events;

import cardgamelibrary.Card;
import cardgamelibrary.Creature;
import cardgamelibrary.Event;
import cardgamelibrary.EventType;

public class CardHealedEvent implements Event {

	private Creature	target;
	private Card			src;
	private int				heal;

	public CardHealedEvent(Creature target, Card src, int heal) {
		this.target = target;
		this.src = src;
		this.heal = heal;
	}

	public Creature getTarget() {
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
