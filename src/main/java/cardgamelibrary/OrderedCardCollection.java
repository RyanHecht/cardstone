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

import cards.FireElement;
import events.CardChosenEvent;
import events.CardDamagedEvent;
import events.CardDrawnEvent;
import events.CardHealedEvent;
import events.CardPlayedEvent;
import events.CardTargetedEvent;
import events.CardZoneChangeEvent;
import events.CardZoneCreatedEvent;
import events.CreatureAttackEvent;
import events.CreatureDiedEvent;
import events.GainElementEvent;
import events.PlayerAttackEvent;
import events.PlayerDamagedEvent;
import events.PlayerHealedEvent;
import events.PlayerTargetedEvent;
import events.StatChangeEvent;
import events.TurnEndEvent;
import events.TurnStartEvent;
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

	public boolean replace(Card a, Card b) {
		int ind = cards.indexOf(a);
		if (ind != -1) {
			cards.set(ind, b);
			return true;
		}
		return false;
	}

	public Player getPlayer() {
		return player;
	}

	public Card getFirstCard() {
		if (cards.size() == 0) {
			// default card to return if collection is empty, will be fatigue card
			// soon.
			return new FireElement(getPlayer());
		}
		return cards.get(0);
	}

	public List<Card> getFirstCards(int numCards) {
		List<Card> cardsGotten = new LinkedList<Card>();
		for (int i = 0; i < numCards; i++) {
			if (i > cards.size()) {
				cardsGotten.add(new FireElement(getPlayer()));
			} else {
				cardsGotten.add(cards.get(i));
			}
		}
		return cardsGotten;
	}

	public List<Effect> handleCardBoardEvent(Event event) {
		List<Effect> results = new LinkedList<>();
		switch (event.getType()) {
		case CARD_DAMAGED:
			results = handleCardDamaged((CardDamagedEvent) event);
			break;
		case CARD_HEALED:
			results = handleCardHealed((CardHealedEvent) event);
		case CARD_DRAWN:
			results = handleDraw((CardDrawnEvent) event);
			break;
		case CARD_PLAYED:
			results = handleCardPlayed((CardPlayedEvent) event);
			break;
		case CARD_ZONE_CHANGED:
			results = handleCardZoneChange((CardZoneChangeEvent) event);
			break;
		case CREATURE_DIED:
			results = handleCreatureDied((CreatureDiedEvent) event);
			break;
		case PLAYER_DAMAGED:
			results = handlePlayerDamaged((PlayerDamagedEvent) event);
			break;
		case PLAYER_HEALED:
			results = handlePlayerHealed((PlayerHealedEvent) event);
		case TURN_START:
			results = handleTurnStart((TurnStartEvent) event);
			break;
		case TURN_END:
			results = handleTurnEnd((TurnEndEvent) event);
			break;
		case CARD_CREATED:
			results = handleCardZoneCreated((CardZoneCreatedEvent) event);
			break;
		case CREATURE_ATTACKED:
			results = handleCreatureAttacked((CreatureAttackEvent) event);
			break;
		case PLAYER_ATTACKED:
			results = handlePlayerAttacked((PlayerAttackEvent) event);
			break;
		case HEALTH_CHANGE:
			results = handleHealthChange((StatChangeEvent) event);
			break;
		case ATTACK_CHANGE:
			results = handleAttackChange((StatChangeEvent) event);
			break;
		case ELEMENT_GAINED:
			results = handleElementGain((GainElementEvent) event);
			break;
		case CARD_CHOSEN:
			results = handleCardChosen((CardChosenEvent) event);
			break;
		case CARD_TARGETED:
			results = handleCardTargeted((CardTargetedEvent) event);
			break;
		case PLAYER_TARGETED:
			results = handlePlayerTargeted((PlayerTargetedEvent) event);
			break;
		default:
			throw new RuntimeException("ERROR: Invalid event type: " + event.getType());
		}

		return results;
	}

	public boolean getChanged() {
		return changed;
	}

	private List<Effect> handleCardTargeted(CardTargetedEvent event) {
		List<Effect> results = new LinkedList<>();
		for (Card c : cards) {
			results.add(c.onCardTarget(event.getTargetter(), event.getTargeted(), getZone(), event.getTargetZone()));
		}
		return results;
	}

	private List<Effect> handlePlayerTargeted(PlayerTargetedEvent event) {
		List<Effect> results = new LinkedList<>();
		for (Card c : cards) {
			results.add(c.onPlayerTarget(event.getTargetter(), event.getTarget(), getZone()));
		}
		return results;
	}

	private List<Effect> handleElementGain(GainElementEvent event) {
		List<Effect> results = new LinkedList<>();
		for (Card c : cards) {
			results.add(c.onElementGain(event.getPlayer(), event.getElementType(), event.getAmount(), getZone()));
		}
		return results;
	}

	private List<Effect> handleAttackChange(StatChangeEvent event) {
		List<Effect> results = new LinkedList<>();
		for (Card c : cards) {
			results.add(c.onAttackChange(event.getTarget(), event.getAmount(), getZone()));
		}
		return results;
	}

	private List<Effect> handleHealthChange(StatChangeEvent event) {
		List<Effect> results = new LinkedList<>();
		for (Card c : cards) {
			results.add(c.onHealthChange(event.getTarget(), event.getAmount(), getZone()));
		}
		return results;
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
		List<Effect> results = new LinkedList<>();
		for (Card c : cards) {
			results.add(c.onCardDrawn(cDrawn.getDrawn(), getZone()));
		}
		return results;
	}

	private List<Effect> handleTurnStart(TurnStartEvent start) {
		List<Effect> results = new LinkedList<>();
		for (Card c : cards) {
			results.add(c.onTurnStart(start.getPlayer(), getZone()));
		}
		return results;
	}

	private List<Effect> handleTurnEnd(TurnEndEvent endTurn) {
		List<Effect> results = new LinkedList<>();
		for (Card c : cards) {
			results.add(c.onTurnEnd(endTurn.getPlayer(), getZone()));
		}
		return results;
	}

	private List<Effect> handleCardDamaged(CardDamagedEvent cDamaged) {
		List<Effect> results = new LinkedList<>();
		for (Card c : cards) {
			results.add(c.onCreatureDamage(cDamaged.getTarget(), cDamaged.getSrc(), cDamaged.getDmg(), getZone()));
		}
		return results;
	}

	private List<Effect> handlePlayerDamaged(PlayerDamagedEvent pDamaged) {
		List<Effect> results = new LinkedList<>();
		for (Card c : cards) {
			results.add(c.onPlayerDamage(pDamaged.getPlayer(), pDamaged.getSrc(), pDamaged.getDmg(), getZone()));
		}
		return results;
	}

	private List<Effect> handleCardHealed(CardHealedEvent cHealed) {
		List<Effect> results = new LinkedList<>();
		for (Card c : cards) {
			results.add(c.onCreatureHeal(cHealed.getTarget(), cHealed.getSrc(), cHealed.getHeal(), getZone()));
		}
		return results;
	}

	private List<Effect> handlePlayerHealed(PlayerHealedEvent pHealed) {
		List<Effect> results = new LinkedList<>();
		for (Card c : cards) {
			results.add(c.onPlayerHeal(pHealed.getTarget(), pHealed.getSrc(), pHealed.getHeal(), getZone()));
		}
		return results;
	}

	private List<Effect> handleCreatureDied(CreatureDiedEvent cDeath) {
		List<Effect> results = new LinkedList<>();
		for (Card c : cards) {
			results.add(c.onCreatureDeath(cDeath.getCreature(), getZone()));
		}
		return results;
	}

	private List<Effect> handleCardPlayed(CardPlayedEvent played) {
		List<Effect> results = new LinkedList<>();
		for (Card c : cards) {
			results.add(c.onCardPlayed(played.getCard(), getZone()));
		}
		return results;
	}

	private List<Effect> handleCardZoneChange(CardZoneChangeEvent change) {
		List<Effect> results = new LinkedList<>();
		for (Card c : cards) {
			results.add(c.onZoneChange(change.getCard(), change.getEnd(), change.getStart(), getZone()));
		}
		return results;
	}

	// When a card is created on the fly and put into a zone instead of going
	// there from somewhere else
	private List<Effect> handleCardZoneCreated(CardZoneCreatedEvent created) {
		List<Effect> results = new LinkedList<>();
		for (Card c : cards) {
			results.add(c.onCardZoneCreated(created.getCard(), created.getLocation(), getZone()));
		}
		return results;
	}

	private List<Effect> handleCreatureAttacked(CreatureAttackEvent cAttack) {
		List<Effect> results = new LinkedList<>();
		for (Card c : cards) {
			results.add(c.onCreatureAttack(cAttack.getAttacker(), cAttack.getTarget(), getZone()));
		}
		return results;
	}

	private List<Effect> handlePlayerAttacked(PlayerAttackEvent pAttack) {
		List<Effect> results = new LinkedList<>();
		for (Card c : cards) {
			results.add(c.onPlayerAttack(pAttack.getAttacker(), pAttack.getTarget(), getZone()));
		}
		return results;
	}

	private List<Effect> handleCardChosen(CardChosenEvent event) {
		List<Effect> results = new LinkedList<>();
		for (Card c : cards) {
			results.add(c.onCardChosen(event.getChooser(), event.getChosen(), getZone()));
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

	/**
	 * Gets the cards in the occ.
	 *
	 * @return the list of cards within the occ.
	 */
	public List<Card> getCards() {
		return cards;
	}

	public JsonObject jsonifyCreatureZoneWithTargets() {
		// should only call on creature zones.
		assert (getZone() == Zone.CREATURE_BOARD);
		JsonObject result = new JsonObject();
		result.addProperty("changed", hasChanged());
		result.addProperty("size", this.size());
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
	public JsonObject jsonifySelf() {
		JsonObject result = new JsonObject();
		result.addProperty("changed", hasChanged());
		result.addProperty("size", this.size());
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

	public JsonObject jsonifySelfWithBack() {
		JsonObject result = new JsonObject();
		result.addProperty("changed", hasChanged());
		result.addProperty("size", this.size());
		List<JsonObject> cardObjects = new ArrayList<>();
		List<JsonObject> cardBacks = new ArrayList<>();
		for (Card c : cards) {
			cardObjects.add(c.jsonifySelf());
			cardBacks.add(c.jsonifySelfBack());
		}
		Gson gson = new Gson();
		result.add("cards", gson.toJsonTree(cardObjects));
		result.add("backs", gson.toJsonTree(cardBacks));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this != null && obj != null && obj instanceof OrderedCardCollection) {
			OrderedCardCollection otherCollection = (OrderedCardCollection) obj;

			if (otherCollection.size() != size()) {
				return false;
			}

			for (int i = 0; i < size(); i++) {
				if (!(getCards().get(i).equals(otherCollection.getCards().get(i)))) {
					return false;
				}
			}
			return true;
		}
		return false;
	}

}
