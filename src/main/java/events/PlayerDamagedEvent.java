package events;

import java.util.List;

import cardgamelibrary.Card;
import cardgamelibrary.Event;
import cardgamelibrary.EventType;
import game.Player;

public class PlayerDamagedEvent implements Event {

	private Card		src;
	private Player	p;
	private int			dmg;

	public PlayerDamagedEvent(Card src, Player p, int dmg) {
		this.src = src;
		this.p = p;
		this.dmg = dmg;
	}

	@Override
	public EventType getType() {
		// TODO Auto-generated method stub
		return EventType.PLAYER_DAMAGED;
	}

	@Override
	public List<Card> getAffected() {
		// TODO Auto-generated method stub
		return null;
	}

	public Card getSrc() {
		return src;
	}

	public Player getPlayer() {
		return p;
	}

	public int getDmg() {
		return dmg;
	}

}
