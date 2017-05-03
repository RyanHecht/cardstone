package game;

import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import cardgamelibrary.AuraCard;
import cardgamelibrary.Board;
import cardgamelibrary.Card;
import cardgamelibrary.Creature;
import cardgamelibrary.Element;
import cardgamelibrary.Event;
import cardgamelibrary.Jsonifiable;
import cardgamelibrary.OrderedCardCollection;
import cardgamelibrary.PlayableCard;
import cardgamelibrary.SpellCard;
import cardgamelibrary.Zone;
import events.CardChosenEvent;
import events.CardPlayedEvent;
import events.CardTargetedEvent;
import events.CreatureAttackEvent;
import events.PlayerAttackEvent;
import events.PlayerTargetedEvent;
import events.TurnEndEvent;
import events.TurnStartEvent;
import logins.ClassByJosh;
import server.CommsWebSocket;
import templates.PlayerChoosesCards;
import templates.TargetsOtherCard;
import templates.TargetsPlayer;

/**
 * Class to represent a game.
 *
 * @author Raghu
 *
 */
public class Game implements Jsonifiable, Serializable {
	private static final int			PLAYER_START_LIFE	= 30;
	private GameState							state							= GameState.IDLE;
	private Board									board;
	private Player								playerOne;
	private Player								playerTwo;
	private int										id;
	private static AtomicInteger	idGenerator				= new AtomicInteger(0);
	private static final String		PACKAGE_PATH			= "cards.";

	// used to keep track of the card that spawns a choose request so we can
	// handle
	// specific behavior once we get a response from the user.
	private PlayerChoosesCards		chooserCard				= null;

	public Game(List<String> firstPlayerCards, List<String> secondPlayerCards, int playerOneId, int playerTwoId) {
		// this.id = ClassByJosh.createNewGame(playerOneId, playerTwoId);
		this.id = idGenerator.incrementAndGet();
		// Initialize both players with starting life.
		playerOne = new Player(PLAYER_START_LIFE, PlayerType.PLAYER_ONE, playerOneId);
		playerTwo = new Player(PLAYER_START_LIFE, PlayerType.PLAYER_TWO, playerTwoId);

		// build decks from the lists of cards.
		OrderedCardCollection deckOne = new OrderedCardCollection(Zone.DECK, playerOne);
		OrderedCardCollection deckTwo = new OrderedCardCollection(Zone.DECK, playerTwo);

		// for formatted names.
		List<String> formattedOne = new LinkedList<>();
		List<String> formattedSecond = new LinkedList<>();

		// format the card names from the lists.
		for (String name : firstPlayerCards) {
			formattedOne.add(name.replaceAll("[^A-Za-z0-9]", "").replaceAll(" ", ""));
		}

		for (String name : secondPlayerCards) {
			formattedSecond.add(name.replaceAll("[^A-Za-z0-9]", "").replaceAll(" ", ""));
		}

		// for first player
		List<PlayableCard> fCards = new ArrayList<>();
		// for second player
		List<PlayableCard> sCards = new ArrayList<>();

		// but how do we register players for all the cards?

		// maybe take in list of card names? Can invoke constructors like so.
		// some card names won't be class names b/c they have punctuation/long names
		// so will probably have to build some sort of map for this.
		// yeah judging by the number of exceptions this is probably no good lmao.
		try {
			// so declare players, then loop over lists of strings for card names,
			// invoking constructors as we go and adding to new list before adding all
			// to the OrderedCardCollections?
			for (String formattedName : formattedOne) {
				Object p = Class.forName(PACKAGE_PATH + formattedName).getConstructor(Player.class).newInstance(playerOne);
				if (p instanceof Creature) {
					fCards.add((Creature) p);
				} else if (p instanceof AuraCard) {
					fCards.add((AuraCard) p);
				} else if (p instanceof SpellCard) {
					fCards.add((SpellCard) p);
				} else if (p instanceof Element) {
					fCards.add((Element) p);
				} else {
					throw new RuntimeException(
							"ERROR: Some sort of invalid card was trying to be added to Player One's deck. Name is : "
									+ formattedName);
				}
			}

			// repeat process with player two's deck.
			for (String formattedName : formattedSecond) {
				Object p = Class.forName(PACKAGE_PATH + formattedName).getConstructor(Player.class).newInstance(playerTwo);
				if (p instanceof Creature) {
					sCards.add((Creature) p);
				} else if (p instanceof AuraCard) {
					sCards.add((AuraCard) p);
				} else if (p instanceof SpellCard) {
					sCards.add((SpellCard) p);
				} else if (p instanceof Element) {
					sCards.add((Element) p);
				} else {
					throw new RuntimeException(
							"ERROR: Some sort of invalid card was trying to be added to Player Two's deck. Name is : "
									+ formattedName);
				}
			}

		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RuntimeException e) {
			System.out.println(e.getMessage());
		}

		// add cards to decks.
		deckOne.addAll(fCards);
		deckTwo.addAll(sCards);

		// shuffle decks.
		deckOne.shuffle();
		deckTwo.shuffle();

		// Some sort of board constructor goes here.
		board = new Board(deckOne, deckTwo);

		// send turnstart to kick off the fun!

		// create turn start event for starting player (active player)
		TurnStartEvent event = new TurnStartEvent(board.getActivePlayer());
		act(event);
	}

	public void setBoard(Board board) {
		this.board = board;
	}

	/**
	 * gets the id of the player who ISN'T the player w/the input id.
	 *
	 * @param playerId
	 *          the id of the player we're trying to find the opponent of.
	 * @return the opposing player's id, or -1 if the input id doesn't belong to
	 *         either player.
	 */
	public int getOpposingPlayerId(int playerId) {
		if (playerOne.getId() == playerId) {
			return playerTwo.getId();
		} else if (playerTwo.getId() == playerId) {
			return playerOne.getId();
		}
		return -1;
	}

	public void endGame(int i) {
		ClassByJosh.endGame(this, i);
	}

	/**
	 * Gets the id of the game.
	 *
	 * @return the game id.
	 */
	public int getId() {
		return id;
	}

	/**
	 * Checks to see if a player with a certain Id is in the game.
	 *
	 * @param playerId
	 *          the id of the player we are looking for.
	 * @return a boolean that represents whether a player with the input id is in
	 *         the game.
	 */
	public boolean inGame(int playerId) {
		return (playerOne.getId() == playerId) || (playerTwo.getId() == playerId);
	}

	/**
	 * Checks to see if it's a player with a certain id's turn.
	 *
	 * @param playerId
	 *          the player id.
	 * @return a boolean representing whether the the player with the input id is
	 *         the current active player. Outputs false if the input playerId is
	 *         not in the game.
	 */
	public boolean isTurn(int playerId) {
		return inGame(playerId) && board.getActivePlayer().getId() == playerId;
	}

	/**
	 * Used to tell the game to wait for a choice input from the user.
	 */
	public void lockState() {
		state = GameState.AWAITING_CHOICE;
	}

	/**
	 * Used to tell the game to stop waiting for a choice input.
	 */
	public void unlockState() {
		state = GameState.IDLE;
	}

	/**
	 * Used to send a message to a player saying their action was valid.
	 *
	 * @param playerId
	 *          the id of the player we are sending the message to.
	 */
	private void sendPlayerActionGood(int playerId) {
		try {
			CommsWebSocket.sendActionOk(playerId);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Used to send messages to players telling them their actions are bad.
	 *
	 * @param playerId
	 *          the id of the player we are sending a message to.
	 * @param message
	 *          the message we are sending them.
	 */
	private void sendPlayerActionBad(int playerId, String message) {
		try {
			CommsWebSocket.sendActionBad(playerId, message);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Sends the board state to both players in the game.
	 */
	private void sendWholeBoardToAllAndDb() {
		try {
			CommsWebSocket.sendWholeBoardSate(this, playerOne.getId());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			CommsWebSocket.sendWholeBoardSate(this, playerTwo.getId());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// ClassByJosh.insertBoardStateIntoDb(this.jsonifySelf(), this.id);
	}

	/**
	 * Sends the board to spectators of the current game.
	 */
	public void sendBoardToSpectators() {

	}

	/**
	 * Handles receiving turn end inputs from front end.
	 *
	 * @param userInput
	 *          a JsonObject representing the user's input.
	 * @param playerId
	 *          the id of the player who sent the action.
	 */
	public void handleTurnend(JsonObject userInput, int playerId) {
		if (!(isTurn(playerId))) {
			// player acting out of turn.
			sendPlayerActionBad(playerId, "Acting out of turn.");
		} else {

			if (state != GameState.IDLE) {
				// if the game state isn't idle, we are awaiting some other input from
				// the user so they can't end their turn.
				sendPlayerActionBad(playerId, "Please make a choice from the presented cards!");
				return;
			}
			sendPlayerActionGood(playerId);
			TurnEndEvent event = new TurnEndEvent(board.getActivePlayer());
			act(event);
			// send board to both players.
			sendWholeBoardToAllAndDb();

			// active player switched.
			try {
				// tell new active player it's their turn
				// System.out.println("Sending turn start messages");
				CommsWebSocket.sendTextMessage(board.getActivePlayer().getId(), "It's your turn!");
				CommsWebSocket.sendTurnStart(board.getActivePlayer().getId(), true);
				CommsWebSocket.sendTurnStart(board.getInactivePlayer().getId(), false);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private void act(Event event) {

		String s = board.legalityProcessEvent(event);
		if (!s.equals("ok")) {
			sendPlayerActionBad(board.getActivePlayer().getId(), s);
		} else {
			board.takeAction(event);
		}
		checkWinners();
	}

	private void checkWinners() {
		if (playerOne.getLife() < 0 || playerTwo.getLife() < 0) {
			if (playerOne.getLife() > 0) {
				endGame(1);
			} else if (playerTwo.getLife() > 0) {
				endGame(2);
			} else {
				endGame(0);
			}
		}
	}

	/**
	 * Handles receiving card targeted inputs from front end.
	 *
	 * @param userInput
	 *          a JsonObject representing the user's input.
	 * @param playerId
	 *          the id of the player who sent the action.
	 */
	public void handleCardTargeted(JsonObject userInput, int playerId) {
		if (isTurn(playerId)) {

			if (state != GameState.IDLE) {
				// if the game state isn't idle, we are awaiting some other input from
				// the user so they can't end their turn.
				sendPlayerActionBad(playerId, "Please make a choice from the presented cards!");
				return;
			}

			Card targetter = board.getCardById(userInput.get("IID1").getAsInt());
			Card targetee = board.getCardById(userInput.get("IID2").getAsInt());

			if (!(targetter.getOwner().getId() == playerId)) {
				// in this case the player is trying to play a card that doesn't belong
				// to them.
				sendPlayerActionBad(playerId, "You can't play your opponent's cards!");
				return;
			}

			if ((targetter instanceof Creature) && (targetee instanceof Creature)) {
				// in this case we have an attack.

				Creature attacker = (Creature) targetter;
				Creature target = (Creature) targetee;

				if (attacker.getOwner().getId() != playerId) {
					sendPlayerActionBad(playerId, "Can't attack using an opponents creature!");
					return;
				}

				if (target.getOwner().getId() == playerId) {
					sendPlayerActionBad(playerId, "Can't attack your own creature!");
					return;
				}

				// at this point we know the attacker belongs to the player who sent the
				// action and the target
				// belongs to the player who didn't send the action (the opponent).

				if (!(attacker.canAttack())) {
					sendPlayerActionBad(playerId, "Creature can't attack anymore!");
					return;
				}

				// at this point we know the event is valid.
				CreatureAttackEvent event = new CreatureAttackEvent(attacker, target);

				// tell player their action was valid.
				sendPlayerActionGood(playerId);

				act(event);

				// send board to both players.
				sendWholeBoardToAllAndDb();
			} else if (targetter instanceof TargetsOtherCard) {
				// we have some sort of card on card action here son.

				// check to see if we can pay the card's cost.
				if (!(board.getActivePlayer().validateCost(targetter.getCost()))) {
					sendPlayerActionBad(playerId, "Cannot play card, insufficient resources.");
					return;
				}

				Zone targetIn = board.getZoneOfCard(targetee);

				// the targeted card wasn't a valid target.
				if (!(((TargetsOtherCard) targetter).cardValidTarget(targetee, targetIn))) {
					sendPlayerActionBad(playerId, "Invalid target!");
					return;
				}

				// targeted card was a valid card.
				// construct appropriate event.
				CardTargetedEvent event = new CardTargetedEvent((TargetsOtherCard) targetter, targetee, targetIn);

				// tell player the action was ok.
				sendPlayerActionGood(playerId);

				// execute action on board.
				act(event);

				// send board to both players.
				sendWholeBoardToAllAndDb();
			} else {
				// well the card that attempted to target something isn't allowed to
				// target stuff. Error time!
				sendPlayerActionBad(playerId, "The card you attempted to use to target something isn't allowed to do that.");
			}
		} else {
			// player acting out of turn.
			sendPlayerActionBad(playerId, "Acting out of turn.");
		}
	}

	/**
	 * Handles players being targeted.
	 *
	 * @param userInput
	 *          a JsonObject representing the user's input.
	 * @param playerId
	 *          the player who submitted the action.
	 */
	public void handlePlayerTargeted(JsonObject userInput, int playerId) {
		if (isTurn(playerId)) {

			if (state != GameState.IDLE) {
				// if the game state isn't idle, we are awaiting some other input from
				// the user so they can't end their turn.
				sendPlayerActionBad(playerId, "Please make a choice from the presented cards!");
				return;
			}

			// this is true if the target player is the player who is acting and
			// false if the target player is the player who is not acting.
			boolean target = userInput.get("self").getAsBoolean();

			// get the card.
			Card card = board.getCardById(userInput.get("IID1").getAsInt());

			if (!(card.getOwner().getId() == playerId)) {
				// in this case the player is trying to play a card that doesn't belong
				// to them.
				sendPlayerActionBad(playerId, "You can't play your opponent's cards!");
				return;
			}

			if (target) {
				// TODO targeted self.
				if (card instanceof TargetsPlayer) {
					if (!(board.getActivePlayer().validateCost(card.getCost()))) {
						sendPlayerActionBad(playerId, "You can't pay that card's cost.");
						return;
					}

					if (!(((TargetsPlayer) card).playerValidTarget(board.getActivePlayer()))) {
						sendPlayerActionBad(playerId, "You can't target yourself with that card.");
						return;
					}

					// construct event.
					PlayerTargetedEvent event = new PlayerTargetedEvent((TargetsPlayer) card, board.getActivePlayer());

					// tell player action was good.
					sendPlayerActionGood(playerId);

					// execute event.
					act(event);

					// send board.
					sendWholeBoardToAllAndDb();
				} else {
					sendPlayerActionBad(playerId, "You can't target yourself with that card.");
				}
			} else {
				// targeted opponent.
				if (card instanceof Creature) {
					// we have an attack going down.
					Creature attacker = (Creature) card;

					if (!(attacker.canAttack())) {
						sendPlayerActionBad(playerId, "That creature can no longer attack.");
						return;
					}

					// creature can attack, so let's attack!

					PlayerAttackEvent event = new PlayerAttackEvent(board.getInactivePlayer(), attacker);

					// tell player their action is valid.
					sendPlayerActionGood(playerId);

					// execute event on board.
					act(event);

					// send board to both players.
					sendWholeBoardToAllAndDb();
				} else if (card instanceof TargetsPlayer) {
					// TODO Targeted opposing player.
					if (!(board.getActivePlayer().validateCost(card.getCost()))) {
						sendPlayerActionBad(playerId, "You can't pay that card's cost.");
						return;
					}

					if (!(((TargetsPlayer) card).playerValidTarget(board.getInactivePlayer()))) {
						sendPlayerActionBad(playerId, "You can't target your opponent with that card.");
						return;
					}

					// construct event.
					PlayerTargetedEvent event = new PlayerTargetedEvent((TargetsPlayer) card, board.getInactivePlayer());

					// tell player action was good.
					sendPlayerActionGood(playerId);

					// execute event.
					act(event);

					// send board.
					sendWholeBoardToAllAndDb();
				} else {
					sendPlayerActionBad(playerId, "You can't target a player with that card.");
				}
			}
		} else {
			sendPlayerActionBad(playerId, "Acting out of turn.");
		}
	}

	/**
	 * Handles users choosing a card from some list we sent them.
	 *
	 * @param userInput
	 * @param playerId
	 */
	public void handleChosen(JsonObject userInput, int playerId) {
		if (isTurn(playerId)) {
			// in this case, since we only enter this state if a choose request is
			// occurring, the user has responded.

			// ensure there is some card prompting the choice saved.
			assertNotNull(chooserCard);

			// TODO retrieve card or list of cards that user has chosen and create
			// events that reflect them .

			// get card the user chose.
			Card chosen = board.getCardById(userInput.get("IID1").getAsInt());

			CardChosenEvent event = new CardChosenEvent(chooserCard, chosen);

			sendPlayerActionGood(playerId);

			act(event);

			// create card played event for the choosing card.
			CardPlayedEvent cEvent = new CardPlayedEvent(chooserCard, board.getOcc(chooserCard.getOwner(), Zone.GRAVE),
					board.getOcc(chooserCard.getOwner(), Zone.HAND));

			act(cEvent);

			sendWholeBoardToAllAndDb();

			// reset the choosing card.
			chooserCard = null;

			// done responding to choose request so change game state again.
			unlockState();
		} else {
			// yeah i'm not sure how this would even happen but better safe than
			// sorry.
			sendPlayerActionBad(playerId, "Acting our of turn.");
		}
	}

	/**
	 * Handles a card being played.
	 *
	 * @param userInput
	 *          a JSON representing the user's input.
	 * @param playerId
	 *          the player who submitted the action.
	 */
	public void handleCardPlayed(JsonObject userInput, int playerId) {
		System.out.println("asdiujqwiuihqwiuibuqwdqiubiua");
		if (isTurn(playerId)) {
			// grab relevant card.
			Card card = board.getCardById(userInput.get("IID1").getAsInt());

			// check to see if game is in a state where cards can be played.
			if (state != GameState.IDLE) {
				// if the game state isn't idle, we are awaiting some other input from
				// the user so they can't end their turn.
				sendPlayerActionBad(playerId, "Please make a choice from the presented cards!");
				return;
			}

			if (!(card.getOwner().getId() == playerId)) {
				// in this case the player is trying to play a card that doesn't belong
				// to them.
				sendPlayerActionBad(playerId, "You can't play your opponent's cards!");
				return;
			}

			if (!(board.getActivePlayer().validateCost(card.getCost()))) {
				// in this case they can't play the card.
				sendPlayerActionBad(playerId, "Cannot pay card's cost.");
				return;
			}

			if (card instanceof TargetsOtherCard) {
				// send player a target request b/c their card requires a target.
				CommsWebSocket.sendTargetRequest(playerId);
				return;
			}

			// the user must make some sort of choice here.
			if (card instanceof PlayerChoosesCards) {
				// get possible options.
				List<Card> options = ((PlayerChoosesCards) card).getOptions(board);

				// create JsonObject to send to front end for user to make a choice.
				JsonObject result = new JsonObject();
				result.addProperty("size", options.size());
				List<JsonObject> cardObjects = new ArrayList<>();
				System.out.println("Number of Options: " + options.size());
				for (Card c : options) {
					System.out.println("card checked");
					cardObjects.add(c.jsonifySelf());
				}
				Gson gson = new Gson();
				result.add("cards", gson.toJsonTree(cardObjects));

				// send to front end.
				try {
					CommsWebSocket.sendChooseRequest(playerId, result);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				// make sure no other choosing cnard is saved.
				assert (chooserCard == null);

				// save the choosing card into the variable.
				chooserCard = (PlayerChoosesCards) card;

				// state should now set to awaiting response.
				lockState();
			}

			Zone z;
			if (card instanceof Creature) {
				z = Zone.CREATURE_BOARD;
			} else if (card instanceof AuraCard) {
				z = Zone.AURA_BOARD;
			} else {
				// spell or element.
				z = Zone.GRAVE;
			}

			// create event representing CardPlayedEvent
			CardPlayedEvent event = new CardPlayedEvent(card, board.getOcc(board.getActivePlayer(), Zone.HAND),
					board.getOcc(board.getActivePlayer(), z));

			// tell player action was ok.
			sendPlayerActionGood(playerId);

			// execute event on board.
			act(event);

			// send board to both players.
			sendWholeBoardToAllAndDb();
		} else {
			sendPlayerActionBad(playerId, "Acting out of turn.");
		}
	}

	/**
	 * Tells you if a player with a certain id is the active player.
	 *
	 * @param playerId
	 *          the id of a player we want to check.
	 * @return a boolean that tells us if the playerId input belongs to the
	 *         current active player.
	 */
	public boolean isActivePlayer(int playerId) {
		return board.getActivePlayer().getId() == playerId;
	}

	@Override
	public JsonObject jsonifySelf() {
		JsonObject payload = new JsonObject();
		payload.add("player1", playerOne.jsonifySelf());
		payload.add("player2", playerTwo.jsonifySelf());
		payload.add("board", board.jsonifySelf());
		return payload;
	}

	@Override
	public JsonObject jsonifySelfChanged() {
		JsonObject payload = new JsonObject();
		payload.add("player1", playerOne.jsonifySelf());
		payload.add("player2", playerTwo.jsonifySelf());
		payload.add("board", board.jsonifySelfChanged());
		return payload;
	}

	// Modify them so that the players can't see eachothers hands
	public JsonObject playerOneJsonify(JsonObject toMod) {
		toMod.get("board").getAsJsonObject().get("hand2").getAsJsonObject().remove("cards");
		return toMod;
	}

	public JsonObject playerTwoJsonify(JsonObject toMod) {
		toMod.get("board").getAsJsonObject().get("hand1").getAsJsonObject().remove("cards");
		return toMod;
	}
}