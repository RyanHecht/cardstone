package cardgamelibary;

import java.util.Iterator;
import java.util.List;
import java.util.Queue;

/**
 * Contains the entire state of a given game
 * @author 42jpa
 *
 */
public class Board {

	List<OrderedCardCollection> cardsInGame;
	Queue<Event> eventQueue;
	Queue<Effect> effectQueue;
	private List<Creature> onBoard;
	
	
	//Basically, any time a player sends a command, events or effects will wind up in q, and then this will be called.
	//This will handle the entire cascade of events placed in the queue, until there are none left.
	private void handleState(){
		while(eventQueue.size() > 1 || effectQueue.size() > 1){
			if(eventQueue.size() > 1){
				// when we handle an event we want to put
				// all effects it produces onto the queue.
				handleEvent(eventQueue.poll());
			}
			else{
				//dead creatures are cleaned up after all events have processed.
				handleDead();
				handleEffect(effectQueue.poll());
			}
		}
	}

	private void handleDead() {
		Iterator<Creature> it = onBoard.iterator();
		while(it.hasNext()){
			Creature c = it.next();
			if(c.isDead()){
				creatureDies(c);
				it.remove();
			}
		}
	}

	private void handleEffect(Effect effect) {
		effect.apply(this);
	}

	private void handleEvent(Event event) {
		List<Card> affected = event.getAffected(this);
		for(Card c : affected){
			for(EventHandler eh : c.getHandlers()){
				if(eh.handles(event)){
					effectQueue.addAll(eh.handle(event));
					
				}
				
			}
		}	
	}
	
}

