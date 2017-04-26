package cardgamelibrary;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import com.google.gson.JsonObject;

import events.CardDamagedEvent;
import events.CardHealedEvent;
import events.CardZoneChangeEvent;
import events.CardZoneCreatedEvent;
import events.CreatureAttackEvent;
import events.CreatureDiedEvent;
import events.GainElementEvent;
import events.PlayerDamagedEvent;
import events.PlayerHealedEvent;
import events.StatChangeEvent;
import events.TurnStartEvent;
import game.Player;
import game.PlayerType;
import server.CommsWebSocket;

/**
 * Contains the entire state of a given game
 *
 * @author Kaushik Raghu Nimmagadda
 *
 */
public class Board implements Jsonifiable {

	Queue<Event>									eventQueue;
	Queue<Effect>									effectQueue;

	private static final int			STARTING_HAND_SIZE	= 6;

	// player one stuff;
	private OrderedCardCollection	deckOne;
	private OrderedCardCollection	handOne;
	private OrderedCardCollection	auraOne;
	private OrderedCardCollection	graveOne;
	private OrderedCardCollection	creatureOne;

	// player two stuff;
	private OrderedCardCollection	deckTwo;
	private OrderedCardCollection	handTwo;
	private OrderedCardCollection	auraTwo;
	private OrderedCardCollection	graveTwo;
	private OrderedCardCollection	creatureTwo;

	// everything in the game;
	List<OrderedCardCollection>		cardsInGame					= new ArrayList<>();

	// currently active player.
	private Player								activePlayer;

	public Board(OrderedCardCollection deckOne, OrderedCardCollection deckTwo) {
		// using LinkedLists but declaring using queue interface.
		// Seems like the best way to handle the queues.
		eventQueue = new LinkedList<Event>();
		effectQueue = new LinkedList<Effect>();

		this.deckOne = deckOne;
		this.deckTwo = deckTwo;

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

		// create turn start event for starting player (active player)
		TurnStartEvent event = new TurnStartEvent(activePlayer);
		takeAction(event);
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
				Event e = eventQueue.poll();

				handleEvent(e);

				// used to send animations off to front end!
				JsonObject animation = new JsonObject();
				JsonObject payload = new JsonObject();

				// send animation for creature combat.
				if (e.getType() == EventType.CREATURE_ATTACKED) {
					CreatureAttackEvent event = (CreatureAttackEvent) e;
					animation.addProperty("eventType", "creatureAttacked");
					payload.addProperty("id1", event.getAttacker().getId());
					payload.addProperty("id2", event.getTarget().getId());
					animation.add("payload", payload);
					sendAnimation(animation);
				}

				// send animation for creature taking damage.
				if (e.getType() == EventType.CARD_DAMAGED) {
					CardDamagedEvent event = (CardDamagedEvent) e;
					animation.addProperty("eventType", "creatureDamaged");
					payload.addProperty("id1", event.getTarget().getId());
					animation.add("payload", payload);
					sendAnimation(animation);
				}

				// send animation for player attacked.
				if (e.getType() == EventType.PLAYER_ATTACKED) {
					// TODO
				}

				// if send animation for player damaged.
				if (e.getType() == EventType.PLAYER_DAMAGED) {
					// TODO
				}

				// TODO go over logic in how changing active player here will work with
				// cards that might depend on that info. Could be a mistake to do that
				// here.
				if (e.getType() == EventType.TURN_START) {
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

			} else {
				handleDead();
				while (effectQueue.size() != 0) {
					handleEffect(effectQueue.poll());
				}
			}
		}
		handleDead();
		if(eventQueue.size() > 0 || effectQueue.size() > 0){
			handleState();
		}
	}

	private void handleDead() {
		Iterator<Card> it = creatureOne.iterator();
		Iterator<Card> itTwo = creatureTwo.iterator();
		while (it.hasNext()) {
			Creature c = (Creature) it.next();
			if (c.isDead()) {
				// creatureDies should create some sort of creatureDied event
				// and use takeAction to set it in motion.
				creatureDies(c);
				it.remove();
			}
		}
		while (itTwo.hasNext()) {
			Creature c = (Creature) itTwo.next();
			if (c.isDead()) {
				// creatureDies should create some sort of creatureDied event
				// and use takeAction to set it in motion.
				creatureDies(c);
				itTwo.remove();
			}
		}
	}

	private void handleEffect(Effect effect) {
		effect.apply(this);
	}

	private void handleEvent(Event event) {
		List<Card> alreadyDone = new ArrayList<>();
		for (OrderedCardCollection occ : cardsInGame) {
			System.out.println("this shoul appear 6x");
			System.out.println(occ.size());
			for (Card c : occ) {
				if (alreadyDone.contains(c)) {
					System.out.println("awdkjbbjhasdbjhb hjesfkjbbhjqwduvyuyv 1565125665213");
				}
				alreadyDone.add(c);
			}
			// collect effects from all cards in game!
			for (Effect e : occ.handleCardBoardEvent(event)) {
				// iterate through all cards in a collection and add their effects to
				// the queue.
				effectQueue.add(e);
			}
		}
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
	private void creatureDies(Creature c) {
		// construct creature died event.
		CreatureDiedEvent cd = new CreatureDiedEvent(c);
		// add to event queue.
		eventQueue.add(cd);
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
		result.add("hand1", handOne.jsonifySelfWithBack());
		result.add("hand2", handTwo.jsonifySelfWithBack());
		result.add("aura1", auraOne.jsonifySelf());
		result.add("aura2", auraTwo.jsonifySelf());
		result.add("creature1", creatureOne.jsonifySelf());
		result.add("creature2", creatureTwo.jsonifySelf());
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
		CardZoneCreatedEvent event = new CardZoneCreatedEvent(summon, targetZone);
		for (OrderedCardCollection occ : cardsInGame) {
			if (occ.getZone() == targetZone && occ.getPlayer() == summon.getOwner()) {
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
	public void damageCard(Creature target, Card src, int dmg) {
		CardDamagedEvent event = new CardDamagedEvent(target, src, dmg);
		target.takeDamage(dmg, src);
		for (OrderedCardCollection occ : cardsInGame) {
			this.effectQueue.addAll(occ.handleCardBoardEvent(event));
		}
	}

	public void damagePlayer(Player target, Card src, int dmg) {
		PlayerDamagedEvent event = new PlayerDamagedEvent(src, target, dmg);
		target.takeDamage(dmg);
		for (OrderedCardCollection occ : cardsInGame) {
			this.effectQueue.addAll(occ.handleCardBoardEvent(event));
		}
	}

	public void healCard(Creature target, Card src, int heal) {
		CardHealedEvent event = new CardHealedEvent(target, src, heal);
		target.heal(heal, src);
		for (OrderedCardCollection occ : cardsInGame) {
			this.effectQueue.addAll(occ.handleCardBoardEvent(event));
		}
	}

	public void healPlayer(Player target, Card src, int heal) {
		PlayerHealedEvent event = new PlayerHealedEvent(target, src, heal);
		target.healDamage(heal);
		for (OrderedCardCollection occ : cardsInGame) {
			this.effectQueue.addAll(occ.handleCardBoardEvent(event));
		}
	}

	public void changeCreatureHealth(Creature target, int amount, Zone z) {
		StatChangeEvent event = new StatChangeEvent(EventType.HEALTH_CHANGE, target, amount);
		target.changeMaxHealthBy(amount);
		for (OrderedCardCollection occ : cardsInGame) {
			this.effectQueue.addAll(occ.handleCardBoardEvent(event));
		}
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
		start.remove(c);
		for (OrderedCardCollection occ : cardsInGame) {
			this.effectQueue.addAll(occ.handleCardBoardEvent(event));
		}
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
	public void changeCreatureAttack(Creature target, int amount, Zone z) {
		StatChangeEvent event = new StatChangeEvent(EventType.ATTACK_CHANGE, target, amount);
		target.changeAttackBy(amount);
		for (OrderedCardCollection occ : cardsInGame) {
			this.effectQueue.addAll(occ.handleCardBoardEvent(event));
		}
	}

	public void givePlayerElement(Player p, ElementType type, int amount) {
		GainElementEvent event = new GainElementEvent(p, type, amount);
		// increase element for player p.
		int curElem = p.getElem(type);
		p.setElement(type, curElem + amount);
		for (OrderedCardCollection occ : cardsInGame) {
			this.effectQueue.addAll(occ.handleCardBoardEvent(event));
		}
	}

}
