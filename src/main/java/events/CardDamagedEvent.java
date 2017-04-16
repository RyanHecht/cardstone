package events;

import java.util.List;

import cardgamelibrary.Card;
import cardgamelibrary.Creature;
import cardgamelibrary.Event;
import cardgamelibrary.EventType;

public class CardDamagedEvent implements Event {

	private Creature	target;
	private Card			src;
	private int				dmg;

	public CardDamagedEvent(Creature target, Card src, int dmg) {
		this.target = target;
		this.src = src;
		this.dmg = dmg;
	}

	public Creature getTarget() {
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

	@Override
	public List<Card> getAffected() {
		// TODO Auto-generated method stub
		return null;
	}

}
