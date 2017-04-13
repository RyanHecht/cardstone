package cardgamelibrary;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import game.Player;

/**
 * A collection of cards. Note that a collection of cards is also a Zone
 * 
 * @author 42jpa
 *
 */
public class OrderedCardCollection implements CardCollection {

	private List<Card>	cards;
	private Zone				zone;

	// every OCC is owned by a player.
	private Player			player;

	public OrderedCardCollection(Zone zone, Player p) {
		this.zone = zone;
		this.player = p;
	}

	public Zone getZone() {
		return zone;
	}

	public List<Effect> handleCardBoardEvent(Event event) {
		List<Effect> results = new ArrayList<>();
		for (Card c : cards) {
			for (EventHandler eh : c.getHandlers()) {
				if (eh.handles(event)) {
					results.addAll(eh.handle(event));
				}
			}
		}
		return results;
	}

	@Override
	public boolean add(Card e) {
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

}
