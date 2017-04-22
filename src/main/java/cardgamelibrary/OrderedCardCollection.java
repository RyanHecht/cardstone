package cardgamelibrary;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import events.CardDamagedEvent;
import events.CardDrawnEvent;
import events.CardZoneChangeEvent;
import events.CardZoneCreatedEvent;
import events.CreatureDiedEvent;
import events.PlayerDamagedEvent;
import events.TurnEndEvent;
import game.Player;

/**
 * A collection of cards. Note that a collection of cards is also a Zone
 *
 * @author 42jpa
 *
 */
public class OrderedCardCollection implements CardCollection, Jsonifiable {

	private List<Card>	cards;
	private Zone				zone;

	// every OCC is owned by a player.
	private Player			player;

	// keeps track of change.
	private boolean			changed	= true;

	public OrderedCardCollection(Zone zone, Player p) {
		this.zone = zone;
		this.player = p;
		this.cards = new LinkedList<>();
	}

	public Zone getZone() {
		return zone;
	}

	public Player getPlayer() {
		return player;
	}

	public List<Effect> handleCardBoardEvent(Event event) {
		List<Effect> results = new ArrayList<>();
		switch (event.getType()) {
		case CARD_DAMAGED:
			results = handleCardDamaged(((CardDamagedEvent) event));
			break;
		case CARD_DRAWN:
			results = handleDraw((CardDrawnEvent) event);
			break;
		case CARD_PLAYED:
			results = handleCardZoneChange((CardZoneChangeEvent) event);
			break;
		case CREATURE_DIED:
			results = handleCreatureDied((CreatureDiedEvent) event);
			break;
		case PLAYER_DAMAGED:
			results = handlePlayerDamaged((PlayerDamagedEvent) event);
			break;
		case TURN_END:
			results = handleTurnEnd((TurnEndEvent) event);
			break;
		case CARD_CREATED:
			results = handleCardZoneCreated((CardZoneCreatedEvent) event);
			break;
		default:
			throw new RuntimeException("ERROR: Invalid event type.");
		}

		return results;
	}

	public boolean getChanged() {
		return changed;
	}

	/**
	 *
	 * Used to handle CardDrawnEvents.
	 *
	 * @param cDrawn
	 *          the cardDraw event in question.
	 * @return a list of effects produced by the OCC due to the event.
	 */
	private List<Effect> handleDraw(CardDrawnEvent cDrawn) {
		List<Effect> results = new ArrayList<>();
		for (Card c : cards) {
			results.add(c.onCardDrawn(cDrawn.getDrawn(), getZone()));
		}
		return results;
	}

	private List<Effect> handleTurnEnd(TurnEndEvent endTurn) {
		List<Effect> results = new ArrayList<>();
		for (Card c : cards) {
			results.add(c.onTurnEnd(endTurn.getPlayer(), getZone()));
		}
		return results;
	}

	private List<Effect> handleCardDamaged(CardDamagedEvent cDamaged) {
		List<Effect> results = new ArrayList<>();
		for (Card c : cards) {
			results.add(c.onDamage(cDamaged.getTarget(), cDamaged.getSrc(), cDamaged.getDmg(), getZone()));
		}
		return results;
	}

	private List<Effect> handlePlayerDamaged(PlayerDamagedEvent pDamaged) {
		List<Effect> results = new ArrayList<>();
		for (Card c : cards) {
			results.add(c.onPlayerDamage(pDamaged.getPlayer(), pDamaged.getSrc(), pDamaged.getDmg(), getZone()));
		}
		return results;
	}

	private List<Effect> handleCreatureDied(CreatureDiedEvent cDeath) {
		List<Effect> results = new ArrayList<>();
		for (Card c : cards) {
			results.add(c.onCreatureDeath(cDeath.getCreature(), getZone()));
		}
		return results;
	}

	private List<Effect> handleCardZoneChange(CardZoneChangeEvent change) {
		List<Effect> results = new ArrayList<>();
		for (Card c : cards) {
			results.add(c.onZoneChange(change.getCard(), change.getStart(), change.getEnd(), getZone()));
		}
		return results;
	}

	// When a card is created on the fly and put into a zone instead of going
	// there from somewhere else
	private List<Effect> handleCardZoneCreated(CardZoneCreatedEvent created) {
		List<Effect> results = new ArrayList<>();
		for (Card c : cards) {
			results.add(c.onCardZoneCreated(created.getCard(), created.getLocation(), getZone()));
		}
		return results;
	}

	@Override
	public boolean add(Card e) {
		System.out.println(cards);
		return cards.add(e);
	}

	@Override
	public boolean addAll(Collection<? extends Card> c) {
		return cards.addAll(c);
	}

	@Override
	public void clear() {
		cards.clear();
	}

	@Override
	public boolean contains(Object o) {
		return cards.contains(o);
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		return cards.containsAll(c);
	}

	@Override
	public boolean isEmpty() {
		return cards.isEmpty();
	}

	@Override
	public Iterator<Card> iterator() {
		return cards.iterator();
	}

	@Override
	public boolean remove(Object o) {
		return cards.remove(o);
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		return cards.removeAll(c);
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		return cards.retainAll(c);
	}

	@Override
	public int size() {
		return cards.size();
	}

	@Override
	public Object[] toArray() {
		return cards.toArray();
	}

	@Override
	public <T> T[] toArray(T[] a) {
		return cards.toArray(a);
	}

	/**
	 * Checks to see if the orderedCardCollection has changed.
	 *
	 * @return a boolean representing whether the OCC has changed.
	 */
	private boolean hasChanged() {
		// TODO Auto-generated method stub
		return true;
	}

	/**
	 * Used to shuffle a deck, will throw error if called on something that isn't
	 * a deck.
	 */
	public void shuffle() {
		// should only shuffle decks.
		assertTrue(zone == Zone.DECK);
		Collections.shuffle(cards);
	}

	@Override
	public JsonObject jsonifySelf() {
		JsonObject result = new JsonObject();
		result.addProperty("changed", hasChanged());
		List<JsonObject> cardObjects = new ArrayList<>();
		System.out.println(cards + " " + cards.size());
		for (Card c : cards) {
			System.out.println("card checked");
			cardObjects.add(c.jsonifySelf());
		}
		Gson gson = new Gson();
		result.add("cards", gson.toJsonTree(cardObjects));
		return result;
	}

	@Override
	public JsonObject jsonifySelfChanged() {
		if (hasChanged()) {
			return jsonifySelf();
		} else {
			JsonObject result = new JsonObject();
			result.addProperty("changed", hasChanged());
			return result;
		}
	}

}
