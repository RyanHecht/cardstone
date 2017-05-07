package cardgamelibrary;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import com.google.gson.JsonObject;

import effects.EffectMaker;
import effects.EffectType;
import events.CardDamagedEvent;
import events.CardHealedEvent;
import events.CardZoneChangeEvent;
import events.CardZoneCreatedEvent;
import events.CreatureAttackEvent;
import events.CreatureDiedEvent;
import events.GainElementEvent;
import events.PlayerAttackEvent;
import events.PlayerDamagedEvent;
import events.PlayerHealedEvent;
import events.StatChangeEvent;
import events.TurnStartEvent;
import game.GameManager;
import game.Player;
import game.PlayerType;
import server.CommsWebSocket;

/**
 * Contains the entire state of a given game
 *
 * @author Kaushik Raghu Nimmagadda
 *
 */
public class Board implements Jsonifiable, Serializable {

	/**
	 * Default value for serial version that eclipse generated.
	 */
	private static final long serialVersionUID = 1L;
	private Queue<Event> eventQueue;
	private LinkedList<Effect> effectQueue;

	private static final int STARTING_HAND_SIZE = 6;

	private final int gameId;

	// player one stuff;
	private OrderedCardCollection deckOne;
	private OrderedCardCollection handOne;
	private OrderedCardCollection auraOne;
	private OrderedCardCollection graveOne;
	private OrderedCardCollection creatureOne;

	// player two stuff;
	private OrderedCardCollection deckTwo;
	private OrderedCardCollection handTwo;
	private OrderedCardCollection auraTwo;
	private OrderedCardCollection graveTwo;
	private OrderedCardCollection creatureTwo;

	private HashSet<Card> alreadyProcessed;

	// everything in the game;
	List<OrderedCardCollection> cardsInGame = new ArrayList<>();

	// currently active player.
	private Player activePlayer;

	// keeps track of turn counter.
	private int turnIndex = 0;

	public Board(OrderedCardCollection deckOne, OrderedCardCollection deckTwo, int gameId) {
		// using LinkedLists but declaring using queue interface.
		// Seems like the best way to handle the queues.
		eventQueue = new LinkedList<Event>();
		effectQueue = new LinkedList<Effect>();

		this.deckOne = deckOne;
		this.deckTwo = deckTwo;
		this.gameId = gameId;

		// initialize all other fields here.
		handOne = new OrderedCardCollection(Zone.HAND, deckOne.getPlayer());
		auraOne = new OrderedCardCollection(Zone.AURA_BOARD, deckOne.getPlayer());
		graveOne = new OrderedCardCollection(Zone.GRAVE, deckOne.getPlayer());
		creatureOne = new OrderedCardCollection(Zone.CREATURE_BOARD, deckOne.getPlayer());

		handTwo = new OrderedCardCollection(Zone.HAND, deckTwo.getPlayer());
		auraTwo = new OrderedCardCollection(Zone.AURA_BOARD, deckTwo.getPlayer());
		graveTwo = new OrderedCardCollection(Zone.GRAVE, deckTwo.getPlayer());
		creatureTwo = new OrderedCardCollection(Zone.CREATURE_BOARD, deckTwo.getPlayer());

		// load all fields into cardsInGame
		cardsInGame.add(this.deckOne);
		cardsInGame.add(this.deckTwo);
		cardsInGame.add(handOne);
		cardsInGame.add(handTwo);
		cardsInGame.add(auraOne);
		cardsInGame.add(auraTwo);
		cardsInGame.add(graveOne);
		cardsInGame.add(graveTwo);
		cardsInGame.add(creatureOne);
		cardsInGame.add(creatureTwo);

		// decide starting player.
		if (Math.random() > 0.5) {
			activePlayer = deckOne.getPlayer();
		} else {
			activePlayer = deckTwo.getPlayer();
		}
		// set up starting hands.
		assignStartingHands();
		alreadyProcessed = new HashSet<>();
	}

	/**
	 * Gets the active player.
	 *
	 * @return the active player.
	 */
	public Player getActivePlayer() {
		return activePlayer;
	}

	/**
	 * Changes the active player to p. used in DemoGame. Note that p must be a
	 * player in the game.
	 * 
	 * @param p
	 *          the player we are making the active player.
	 */
	public void setActivePlayer(Player p) {
		activePlayer = p;
	}

	/**
	 * Gets the inactive player.
	 *
	 * @return the inactive player.
	 */
	public Player getInactivePlayer() {
		Player activePlayer = getActivePlayer();
		if (deckOne.getPlayer().equals(activePlayer)) {
			// player one is active so we return player two.
			return deckTwo.getPlayer();
		} else {
			// player two was active so we return player one.
			return deckOne.getPlayer();
		}
	}

	public String getCards() {
		return Arrays.toString(cardsInGame.toArray());
	}

	// This will be used whenever a player
	// wants to perform an event.
	public void takeAction(Event event) {
		eventQueue.add(event);
		handleState();
	}

	public void drawCard(Player player) {
		OrderedCardCollection deck = getOcc(player, Zone.DECK);
		// add first card from deck to hand.
		addCardToOcc(deck.getFirstCard(), getOcc(activePlayer, Zone.HAND), deck);
	}

	// Basically, any time a player sends a command, events or effects will wind
	// up in q, and then this will be called.
	// This will handle the entire cascade of events placed in the queue, until
	// there are none left.
	private void handleState() {
		while (eventQueue.size() >= 1 || effectQueue.size() >= 1) {
			if (eventQueue.size() >= 1) {
				alreadyProcessed.clear();
				// when we handle an event we want to put
				// all effects it produces onto the queue.
				Event e = eventQueue.poll();

				// used to send animations off to front end!
				JsonObject animation = new JsonObject();

				// send animation for creature combat.
				if (e.getType() == EventType.CREATURE_ATTACKED) {
					CreatureAttackEvent event = (CreatureAttackEvent) e;
					animation.addProperty("eventType", "creatureAttacked");
					animation.addProperty("id1", event.getAttacker().getId());
					animation.addProperty("id2", event.getTarget().getId());
					sendAnimation(animation);
				}

				// send animation for creature taking damage.
				if (e.getType() == EventType.CARD_DAMAGED) {
					CardDamagedEvent event = (CardDamagedEvent) e;
					animation.addProperty("eventType", "creatureDamaged");
					animation.addProperty("id1", event.getTarget().getId());
					sendAnimation(animation);
				}

				// send animation for player attacked.
				if (e.getType() == EventType.PLAYER_ATTACKED) {
					// TODO
					PlayerAttackEvent event = (PlayerAttackEvent) e;
					animation.addProperty("eventType", "playerAttacked");
					animation.addProperty("id1", event.getAttacker().getId());
					animation.addProperty("target", event.getTarget().getId());
					sendAnimation(animation);
				}

				// if send animation for player damaged.
				if (e.getType() == EventType.PLAYER_DAMAGED) {
					// TODO
					PlayerDamagedEvent event = (PlayerDamagedEvent) e;
					animation.addProperty("eventType", "playerDamaged");
					animation.addProperty("playerId", event.getPlayer().getId());
					sendAnimation(animation);
				}

				// TODO go over logic in how changing active player here will work with
				// cards that might depend on that info. Could be a mistake to do that
				// here.
				if (e.getType() == EventType.TURN_START) {
					// increment turn index.
					turnIndex++;
					// give player who is starting turn their resources!
					activePlayer.startTurn();
					OrderedCardCollection activeDeck = getOcc(activePlayer, Zone.DECK);
					// add first card from deck to hand.
					addCardToOcc(activeDeck.getFirstCard(), getOcc(activePlayer, Zone.HAND), activeDeck);
				}

				if (e.getType() == EventType.TURN_END) {
					// switch the active player.
					if (activePlayer.getPlayerType() == PlayerType.PLAYER_ONE) {
						activePlayer = deckTwo.getPlayer();
					} else {
						activePlayer = deckOne.getPlayer();
					}
					eventQueue.add(new TurnStartEvent(activePlayer));
					System.out.println("Active Player Id: " + activePlayer.getId());
				}

				// handle event.
				handleEvent(e);

			} else {
				handleDead();
				while (effectQueue.size() != 0) {
					Effect e = effectQueue.pop();
					handleEffect(e);
					if (e.hasNext()) {
						effectQueue.addFirst(e);
					}
				}
			}
		}
		handleDead();
		if (eventQueue.size() > 0 || effectQueue.size() > 0) {
			handleState();
		}
	}

	private void handleDead() {
		Iterator<Card> it = creatureOne.iterator();
		Iterator<Card> itTwo = creatureTwo.iterator();
		for (Card c : creatureOne) {
			System.out.println(c.getName());
		}
		while (it.hasNext()) {
			CreatureInterface c = (CreatureInterface) it.next();
			if (c.isDead()) {
				// creatureDies should create some sort of creatureDied event
				// and use takeAction to set it in motion.
				creatureDies(c);
				it.remove();
			}
		}
		while (itTwo.hasNext()) {
			CreatureInterface c = (CreatureInterface) itTwo.next();
			if (c.isDead()) {
				// creatureDies should create some sort of creatureDied event
				// and use takeAction to set it in motion.
				creatureDies(c);
				itTwo.remove();
			}
		}
	}

	public void handleEffect(Effect effect) {
		if (effect.getType() == EffectType.MAKER) {
			EffectMaker maker = (EffectMaker) effect;
			effect = maker.getEffect(this);
		}
		Effect past = effect;
		effect = preprocessEffect(effect);
		while (past != effect) {
			past = effect;
			if (effect.getType() == EffectType.MAKER) {
				EffectMaker maker = (EffectMaker) effect;
				effect = maker.getEffect(this);
			}
			effect = preprocessEffect(effect);
		}
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
		Event e = event.getNext(this);
		if (e.getType() != EventType.EMPTY) {
			handleEvent(e);
		}
	}

	public String legalityProcessEvent(Event event) {
		String complaint = "ok";
		for (OrderedCardCollection occ : cardsInGame) {
			for (Card c : occ) {
				if (c.onProposedLegalityEvent(event, occ.getZone())) {
					complaint = c.getComplaint(event, occ.getZone());
				}
			}
		}
		return complaint;
	}

	private Effect preprocessEffect(Effect effect) {
		for (OrderedCardCollection occ : cardsInGame) {
			for (Card c : occ) {
				if (!alreadyProcessed.contains(c)) {
					if (c.onProposedEffect(effect, occ.getZone())) {
						alreadyProcessed.add(c);
						effect = c.getNewProposition(effect, occ.getZone());
						preprocessEffect(effect);
					}
				}
			}
		}
		return effect;
	}

	/**
	 * Sends animation to both players in a game.
	 *
	 * @param message
	 *          the message being sent.
	 */
	private void sendAnimation(JsonObject message) {
		try {
			// send animation to both players.
			GameManager.addAnim(message, gameId);
			CommsWebSocket.sendAnimation(deckOne.getPlayer().getId(), message);
			CommsWebSocket.sendAnimation(deckTwo.getPlayer().getId(), message);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	/**
	 * Used to assign players their starting hands.
	 */
	private void assignStartingHands() {
		Iterator<Card> itOne = deckOne.iterator();
		Iterator<Card> itTwo = deckTwo.iterator();

		int i = 0;
		while (i < STARTING_HAND_SIZE) {
			if (itOne.hasNext()) {
				handOne.add(itOne.next());
				itOne.remove();
			}
			if (itTwo.hasNext()) {
				handTwo.add(itTwo.next());
				itTwo.remove();
			}
			i++;
		}
	}

	/**
	 * Given some specifications finds an orderedCardCollection on the board.
	 *
	 * @param p
	 *          the player who the OCC we are looking for belongs to.
	 * @param z
	 *          the zone of the OCC we are looking for.
	 * @return the OCC we are looking for (in zone z, owned by player p).
	 */
	public OrderedCardCollection getOcc(Player p, Zone z) {
		switch (p.getPlayerType()) {
		case PLAYER_ONE:
			switch (z) {
			case CREATURE_BOARD:
				return creatureOne;
			case AURA_BOARD:
				return auraOne;
			case HAND:
				return handOne;
			case DECK:
				return deckOne;
			case GRAVE:
				return graveOne;
			default:
				throw new RuntimeException("ERROR: Illegal Zone in getOcc (Board.java)");
			}
		case PLAYER_TWO:
			switch (z) {
			case CREATURE_BOARD:
				return creatureTwo;
			case AURA_BOARD:
				return auraTwo;
			case HAND:
				return handTwo;
			case DECK:
				return deckTwo;
			case GRAVE:
				return graveTwo;
			default:
				throw new RuntimeException("ERROR: Illegal Zone in getOcc (Board.java)");
			}
		default:
			throw new RuntimeException("ERROR: Illegal player enum in getOcc (Board.java)");
		}
	}

	public void setOcc(OrderedCardCollection cards) {
		System.out.println(cards.getPlayer().getPlayerType().ordinal());
		switch (cards.getPlayer().getPlayerType()) {
		case PLAYER_ONE:
			switch (cards.getZone()) {
			case CREATURE_BOARD:
				// TODO enfore some sort of check here to ensure
				// everything is a creature? I mean we're not gonna
				// use this in the logic, it's only for testing...
				creatureOne = cards;
				break;
			case AURA_BOARD:
				// again, some sort of check here.
				auraOne = cards;
				break;
			case HAND:
				handOne = cards;
				break;
			case DECK:
				deckOne = cards;
				break;
			case GRAVE:
				graveOne = cards;
				break;
			default:
				throw new RuntimeException("ERROR: Illegal Zone in setOcc (Board.java)");
			}
			break;
		case PLAYER_TWO:
			switch (cards.getZone()) {
			case CREATURE_BOARD:
				// TODO enforce some sort of check here to ensure
				// everything is a creature? I mean we're not gonna
				// use this in the logic, it's only for testing...
				creatureTwo = cards;
				break;
			case AURA_BOARD:
				// again, some sort of check here.
				auraTwo = cards;
				break;
			case HAND:
				handTwo = cards;
				break;
			case DECK:
				deckTwo = cards;
				break;
			case GRAVE:
				graveTwo = cards;
				break;
			default:
				throw new RuntimeException("ERROR: Illegal Zone in setOcc (Board.java)");
			}
			break;
		default:
			throw new RuntimeException("ERROR: Illegal player enum in setOcc (Board.java)");
		}
	}

	/**
	 * Called whenever a creature dies.
	 *
	 * @param c
	 *          the creature that died.
	 */
	private void creatureDies(CreatureInterface c) {
		// construct creature died event.
		CreatureDiedEvent cd = new CreatureDiedEvent(c);
		// add to event queue.
		eventQueue.add(cd);

		// send animations.
		JsonObject animation = new JsonObject();
		animation.addProperty("eventType", "cardDied");
		animation.add("id1", c.jsonifySelf());

		sendAnimation(animation);
	}

	public OrderedCardCollection getPlayerOneCreatures() {
		return creatureOne;
	}

	public OrderedCardCollection getPlayerTwoCreatures() {
		return creatureTwo;
	}

	public Card getCardById(int id) {
		for (OrderedCardCollection occ : cardsInGame) {
			for (Card c : occ) {
				if (c.getId() == id) {
					return c;
				}
			}
		}
		throw new IllegalArgumentException("ERROR: No card with id" + id + " found.");
	}

	@Override
	public JsonObject jsonifySelf() {
		JsonObject result = new JsonObject();
		result.addProperty("deckOne", deckOne.size());
		result.addProperty("deckTwo", deckTwo.size());

		// player one is the active player!
		if (activePlayer.getPlayerType() == PlayerType.PLAYER_ONE) {
			result.add("hand1", handOne.jsonifySelfWithZone());
			result.add("hand2", handTwo.jsonifySelfWithBack());
			result.add("aura1", auraOne.jsonifySelfWithZone());
			result.add("aura2", auraTwo.jsonifySelf());
			result.add("creature1", creatureOne.jsonifySelfWithZone());
			result.add("creature2", creatureTwo.jsonifySelfWithZone());
		} else {
			// player two is the active player.
			result.add("hand1", handOne.jsonifySelfWithBack());
			result.add("hand2", handTwo.jsonifySelfWithZone());
			result.add("aura1", auraOne.jsonifySelf());
			result.add("aura2", auraTwo.jsonifySelfWithZone());
			result.add("creature1", creatureOne.jsonifySelfWithZone());
			result.add("creature2", creatureTwo.jsonifySelfWithZone());
		}

		return result;
	}

	@Override
	public JsonObject jsonifySelfChanged() {
		JsonObject result = new JsonObject();
		result.addProperty("deckOne", deckOne.size());
		result.addProperty("deckTwo", deckTwo.size());
		result.add("hand1", handOne.jsonifySelfWithBack());
		result.add("hand2", handTwo.jsonifySelfWithBack());
		result.add("aura1", auraOne.jsonifySelfChanged());
		result.add("aura2", auraTwo.jsonifySelfChanged());
		result.add("creature1", creatureOne.jsonifySelfChanged());
		result.add("creature2", creatureTwo.jsonifySelfChanged());
		return result;
	}

	/**
	 * Transforms a card from one card to another.
	 *
	 * @param target
	 *          the card to transform.
	 * @param result
	 *          what it transforms into.
	 * @param targetZone
	 *          the zone to put the new card into.
	 */
	public void transformCard(Card target, Card result, Zone targetZone) {
		CardZoneCreatedEvent event = new CardZoneCreatedEvent(result, targetZone);
		for (OrderedCardCollection occ : cardsInGame) {
			occ.remove(target);
			if (occ.getZone() == targetZone && occ.getPlayer() == result.getOwner()) {
				occ.add(result);
			}
			this.effectQueue.addAll(occ.handleCardBoardEvent(event));
		}

	}

	/**
	 * Summons a card to a zone.
	 *
	 * @param summon
	 *          the card to summon.
	 * @param targetZone
	 *          the zone to summon to.
	 */
	public void summonCard(Card summon, Zone targetZone) {
		System.out.println("putting into target zone");
		CardZoneCreatedEvent event = new CardZoneCreatedEvent(summon, targetZone);
		System.out.println(summon.getOwner().getId() + "is the ai " + summon.getOwner());
		for (OrderedCardCollection occ : cardsInGame) {
			System.out.println(occ.getPlayer());
			System.out.println(occ.getZone().name() + " is the zone" + targetZone.name());
			if (occ.getZone() == targetZone && occ.getPlayer() == summon.getOwner()) {
				System.out.println("found the zone");
				occ.add(summon);
			}
			this.effectQueue.addAll(occ.handleCardBoardEvent(event));
		}
	}

	/**
	 * Inflicts damage on a card.
	 *
	 * @param target
	 *          the target card.
	 * @param src
	 *          the source of the damage.
	 * @param dmg
	 *          the amount of damage.
	 */
	public void damageCard(CreatureInterface target, Card src, int dmg) {
		CardDamagedEvent event = new CardDamagedEvent(target, src, dmg);
		target.takeDamage(dmg, src);
		eventQueue.add(event);
	}

	public void damagePlayer(Player target, Card src, int dmg) {
		PlayerDamagedEvent event = new PlayerDamagedEvent(src, target, dmg);
		target.takeDamage(dmg);
		eventQueue.add(event);
	}

	public void healCard(CreatureInterface target, Card src, int heal) {
		CardHealedEvent event = new CardHealedEvent(target, src, heal);
		target.heal(heal, src);
		eventQueue.add(event);
	}

	public void healPlayer(Player target, Card src, int heal) {
		PlayerHealedEvent event = new PlayerHealedEvent(target, src, heal);
		target.healDamage(heal);
		eventQueue.add(event);
	}

	public void changeCreatureHealth(CreatureInterface target, int amount) {
		StatChangeEvent event = new StatChangeEvent(EventType.HEALTH_CHANGE, target, amount);
		target.changeMaxHealthBy(amount);
		eventQueue.add(event);
	}

	/**
	 * Used to transfer a card from one OCC to another.
	 *
	 * @param c
	 *          the card that is moving.
	 * @param destination
	 *          the OCC the card c is going to.
	 * @param start
	 *          the OCC the card c is starting in.
	 */
	public void addCardToOcc(Card c, OrderedCardCollection destination, OrderedCardCollection start) {
		CardZoneChangeEvent event = new CardZoneChangeEvent(c, destination, start);
		destination.add(c);
		// System.out.println("DOG " + start.size());
		start.remove(c);
		// System.out.println("CAT " + start.size());

		// animation sending:
		if (destination.getZone() == Zone.HAND) {
			JsonObject animation = new JsonObject();

			// this is card going to hand.
			animation.addProperty("eventType", "cardDrawn");
			animation.addProperty("playerId", c.getOwner().getId());

			sendAnimation(animation);
		} else if (start.getZone() == Zone.HAND && destination.getZone() == Zone.CREATURE_BOARD && c.isA(Creature.class)) {
			JsonObject animation = new JsonObject();

			// creature was played
			animation.addProperty("eventType", "cardPlayed");
			animation.add("card", c.jsonifySelf());

			sendAnimation(animation);
		} else if (start.getZone() == Zone.HAND && destination.getZone() == Zone.AURA_BOARD && c.isA(AuraCard.class)) {
			JsonObject animation = new JsonObject();

			// aura was played.
			animation.addProperty("eventType", "cardPlayed");
			animation.add("card", c.jsonifySelf());

			sendAnimation(animation);
		} else if (start.getZone() == Zone.AURA_BOARD && destination.getZone() == Zone.GRAVE && c.isA(AuraCard.class)) {
			JsonObject animation = new JsonObject();

			// an aura is destroyed here.
			animation.addProperty("eventType", "cardDied");
			animation.add("card", c.jsonifySelf());

			sendAnimation(animation);
		} else if (start.getZone() == Zone.HAND && destination.getZone() == Zone.GRAVE && c.isA(SpellCard.class)) {
			JsonObject animation = new JsonObject();

			// spell was played.
			animation.addProperty("eventType", "cardPlayed");
			animation.add("card", c.jsonifySelf());

			sendAnimation(animation);
		} else if (start.getZone() == Zone.HAND && destination.getZone() == Zone.GRAVE && c instanceof Element) {
			JsonObject animation = new JsonObject();
			animation.addProperty("eventType", "cardPlayed");
			animation.add("card", c.jsonifySelf());
			sendAnimation(animation);
		}

		if (c.isA(CreatureInterface.class) && destination.getZone() == Zone.GRAVE) {
			creatureDies((CreatureInterface) c);
		}

		eventQueue.add(event);
	}

	/**
	 * Changes a creature's attack.
	 *
	 * @param target
	 *          the target creature
	 * @param amount
	 *          the amount to change the stat by.
	 * @param z
	 *          the zone the card is in.
	 */
	public void changeCreatureAttack(CreatureInterface target, int amount, Zone z) {
		StatChangeEvent event = new StatChangeEvent(EventType.ATTACK_CHANGE, target, amount);
		target.changeAttackBy(amount);
		eventQueue.add(event);

	}

	public void givePlayerElement(Player p, ElementType type, int amount) {
		GainElementEvent event = new GainElementEvent(p, type, amount);
		// increase element for player p.
		int curElem = p.getElem(type);
		p.setElement(type, curElem + amount);
		eventQueue.add(event);

	}

	public void applyToCard(Card toApply, Card newCard) {
		for (OrderedCardCollection occ : cardsInGame) {
			if (occ.replace(toApply, newCard)) {
				break;
			}
		}
	}

	/**
	 * Gets the turn index of the board.
	 *
	 * @return the turn index.
	 */
	public int getTurnIndex() {
		return turnIndex;
	}

	/**
	 * Gets the zone of a given card.
	 *
	 * @param c
	 *          the card we are looking for the zone of.
	 * @return the zone of the card or an illegalargumentexception if the card
	 *         wasn't found on the board.
	 */
	public Zone getZoneOfCard(Card c) {
		for (OrderedCardCollection occ : cardsInGame) {
			if (occ.contains(c)) {
				return occ.getZone();
			}
		}
		throw new IllegalArgumentException("Tried to get Zone of card that wasn't found in game");
	}

	/**
	 * Gets all cards on the board.
	 *
	 * @return a list of all orderedcardcollections in the game.
	 */
	public List<OrderedCardCollection> getAllCards() {
		return cardsInGame;
	}

	@Override
	public boolean equals(Object obj) {
		if (this != null && obj != null && obj instanceof Board) {
			Board board = (Board) obj;
			// check card equality in all ordered card collections.
			for (int i = 0; i < cardsInGame.size(); i++) {
				if (!(cardsInGame.get(i).equals(board.getAllCards().get(i)))) {
					return false;
				}
			}
			return true;
		}
		return false;
	}
}
