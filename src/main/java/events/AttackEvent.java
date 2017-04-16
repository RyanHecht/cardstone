package events;

import java.util.List;

import cardgamelibrary.Card;
import cardgamelibrary.Creature;
import cardgamelibrary.Event;
import cardgamelibrary.EventType;

public class AttackEvent implements Event {
	private Creature	attacker;
	private Creature	target;

	public AttackEvent(Creature attacker, Creature target) {
		this.attacker = attacker;
		this.target = target;
	}

	@Override
	public EventType getType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Card> getAffected() {
		// TODO Auto-generated method stub
		return null;
	}

}
