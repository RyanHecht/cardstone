package cardgamelibrary;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import events.CardDamagedEvent;
import events.CardDrawnEvent;
import events.CardZoneChangeEvent;
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
		default:
			throw new RuntimeException("ERROR: Invalid event type.");
		}

		return results;
	}

	/**
	 * Used to handle CardDrawnEvents.
	 *
	 * @param cDrawn
	 *          the cardDraw event in question.
	 * @return a list of effects produced by the OCC due to the event.
	 */
	private List<Effect> handleDraw(CardDrawnEvent cDrawn) {
		List<Effect> results = new ArrayList<>();
		for (Card c : cards) {
			for (Card drawn : cDrawn.getDrawn()) {
				results.add(c.cardDrawn(drawn));
			}
		}
		return results;
	}

	private List<Effect> handleTurnEnd(TurnEndEvent endTurn) {
		List<Effect> results = new ArrayList<>();
		for (Card c : cards) {
			results.add(c.onTurnEnd());
		}
		return results;
	}

	private List<Effect> handleCardDamaged(CardDamagedEvent cDamaged) {
		List<Effect> results = new ArrayList<>();
		for (Card c : cards) {
			results.add(c.onDamage(cDamaged.getTarget(), cDamaged.getSrc(), cDamaged.getDmg()));
		}
		return results;
	}

	private List<Effect> handlePlayerDamaged(PlayerDamagedEvent pDamaged) {
		List<Effect> results = new ArrayList<>();
		for (Card c : cards) {
			results.add(c.onPlayerDamage(pDamaged.getPlayer(), pDamaged.getSrc(), pDamaged.getDmg()));
		}
		return results;
	}

	private List<Effect> handleCreatureDied(CreatureDiedEvent cDeath) {
		List<Effect> results = new ArrayList<>();
		for (Card c : cards) {
			results.add(c.creatureDied(cDeath.getCreature()));
		}
		return results;
	}

	private List<Effect> handleCardZoneChange(CardZoneChangeEvent change) {
		List<Effect> results = new ArrayList<>();
		for (Card c : cards) {
			results.add(c.onZoneChange(change.getCard(), change.getStart(), change.getEnd()));
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
	
	public String jsonifySelf(){
		JsonObject result = new JsonObject();
		List<JsonObject> cardObjects = new ArrayList<>();
		for(Card c : cards){
			cardObjects.add(c.jsonifySelf());
		}
		Gson gson = new Gson();
		return gson.toJson(cardObjects);
	}

}
