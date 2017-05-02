package cardgamelibrary;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import events.EmptyEvent;

public class Event {

	public static AtomicInteger ids;
	private int id;
	private LinkedList<Event> nextEvents;
	
	
	public Event(){
		this.id = Event.ids.incrementAndGet();
		this.nextEvents = new LinkedList<Event>();
	}
	
	
	public int getId(){
		return this.id;
	}
	
	public EventType getType(){
		return EventType.EMPTY;
	};

	
	
	public Event getNext(Board board){
		if(nextEvents.size() == 0){
			return new EmptyEvent();
		}
		else{
			Event e = nextEvents.pop();
			if(e.shouldExecute(board)){
				e.setNexts(nextEvents);
				return e;
			}
			return getNext(board);
		}
	}
	
	private void setNexts(LinkedList<Event> nextEvents) {
		this.nextEvents = nextEvents;
	}


	public void addEvent(Event e){
		nextEvents.add(e);
	}


	private boolean shouldExecute(Board board) {
		return true;
	}
	
}
