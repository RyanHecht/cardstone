package events;

import cardgamelibrary.Card;
import cardgamelibrary.Element;
import cardgamelibrary.ElementType;
import cardgamelibrary.Event;
import cardgamelibrary.EventType;
import game.Player;

public class AppliedDevotionEvent extends Event{

	private Player target;
	private Card src;
	private ElementType type;

	public EventType getType(){
		return EventType.SET_DEVOTION;
	};
	
	public AppliedDevotionEvent(Player target, ElementType type, Card src){
		this.target = target;
		this.src = src;
		this.type = type;
	}
	
	public Player getTarget(){
		return target;
	}
	
	public Card getSrc(){
		return src;
	}

	
}
