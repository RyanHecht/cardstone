package events;

import cardgamelibrary.Element;
import cardgamelibrary.Event;
import cardgamelibrary.EventType;
import game.Player;

public class AppliedDevotionEvent extends Event{

	private Player target;
	private Element src;

	public EventType getType(){
		return EventType.SET_DEVOTION;
	};
	
	public AppliedDevotionEvent(Player target, Element src){
		this.target = target;
		this.src = src;
	}
	
	public Player getTarget(){
		return target;
	}
	
	public Element getSrc(){
		return src;
	}

	
}
