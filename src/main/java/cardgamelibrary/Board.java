package cardgamelibrary;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import com.google.gson.JsonObject;

import game.Player;
import server.MessageTypeEnum;

/**
 * Contains the entire state of a given game
 *
 * @author Kaushik Raghu Nimmagadda
 *
 */
public class Board {

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

	// everything in the game;
	List<OrderedCardCollection>		cardsInGame	= new ArrayList<>();

	public Board(OrderedCardCollection deckOne, OrderedCardCollection deckTwo) {
		// using LinkedLists but declaring using queue interface.
		// Seems like the best way to handle the queues.
		eventQueue = new LinkedList<Event>();
		effectQueue = new LinkedList<Effect>();
		onBoard = new ArrayList<Creature>();

		this.deckOne = deckOne;
		this.deckTwo = deckTwo;

		// initialize all other fields here.

		// load all fields into cardsInGame
		cardsInGame.add(this.deckOne);
		cardsInGame.add(this.deckTwo);
		cardsInGame.add(handOne);
		cardsInGame.add(handTwo);
		cardsInGame.add(auraOne);
		cardsInGame.add(auraTwo);
		cardsInGame.add(graveOne);
		cardsInGame.add(graveTwo);
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
		while (eventQueue.size() >= 1 || effectQueue.size() >= 1) {
			if (eventQueue.size() >= 1) {
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
		for (OrderedCardCollection occ : cardsInGame) {
			// collect effects from all cards in game!
			for (Effect e : occ.handleCardBoardEvent(event)) {
				// iterate through all cards in a collection and add their effects to
				// the queue.
				effectQueue.add(e);
			}
		}
	}

	private void creatureDies(Creature c) {
		// TODO: figure out this method!
	}

	private void draw(int numCards, Player p) {

	}
	
	public JsonObject jsonifySelf(){
		JsonObject result = new JsonObject();
	}

	public List<Creature> getOnBoard() {
		return onBoard;
	}

}
