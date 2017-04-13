package cardgamelibrary;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import game.Player;

/**
 * Contains the entire state of a given game
 *
 * @author Kaushik Raghu Nimmagadda
 *
 */
public class Board {

	List<OrderedCardCollection>		cardsInGame;
	Queue<Event>									eventQueue;
	Queue<Effect>									effectQueue;
	private List<Creature>				onBoard;

	// player one stuff;
	private OrderedCardCollection	deckOne;
	private OrderedCardCollection	handOne;
	private OrderedCardCollection	auraOne;
	private OrderedCardCollection	graveOne;

	// player two stuff;
	private OrderedCardCollection	deckTwo;
	private OrderedCardCollection	handTwo;
	private OrderedCardCollection	auraTwo;
	private OrderedCardCollection	graveTwo;

	public Board(OrderedCardCollection deckOne, OrderedCardCollection deckTwo) {
		// using LinkedLists but declaring using queue interface.
		// Seems like the best way to handle the queues.
		eventQueue = new LinkedList<Event>();
		effectQueue = new LinkedList<Effect>();
		onBoard = new ArrayList<Creature>();
	}

	// This will be used whenever a player
	// wants to perform an event.
	public void takeAction(Event event) {
		eventQueue.add(event);
		handleState();
	}

	// Basically, any time a player sends a command, events or effects will wind
	// up in q, and then this will be called.
	// This will handle the entire cascade of events placed in the queue, until
	// there are none left.
	private void handleState() {
		while (eventQueue.size() > 1 || effectQueue.size() > 1) {
			if (eventQueue.size() > 1) {
				// when we handle an event we want to put
				// all effects it produces onto the queue.
				handleEvent(eventQueue.poll());
			} else {
				// dead creatures are cleaned up after all events have processed.
				handleDead();
				handleEffect(effectQueue.poll());
			}
		}
	}

	private void handleDead() {
		Iterator<Creature> it = onBoard.iterator();
		while (it.hasNext()) {
			Creature c = it.next();
			if (c.isDead()) {
				// creatureDies should create some sort of creatureDied event
				// and use takeAction to set it in motion.
				creatureDies(c);
				it.remove();
			}
		}
	}

	private void handleEffect(Effect effect) {
		effect.apply(this);
	}

	private void handleEvent(Event event) {
		List<Card> affected = event.getAffected();
		for (Card c : affected) {
			for (EventHandler eh : c.getHandlers()) {
				if (eh.handles(event)) {
					effectQueue.addAll(eh.handle(event));

				}

			}
		}
	}

	private void draw(int numCards, Player p) {

	}

}
