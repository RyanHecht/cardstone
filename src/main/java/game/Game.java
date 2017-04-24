package game;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import com.google.gson.JsonObject;

import cardgamelibrary.AuraCard;
import cardgamelibrary.Board;
import cardgamelibrary.Card;
import cardgamelibrary.Creature;
import cardgamelibrary.Jsonifiable;
import cardgamelibrary.OrderedCardCollection;
import cardgamelibrary.PlayableCard;
import cardgamelibrary.SpellCard;
import cardgamelibrary.Zone;
import cards.templates.TargetsOtherCard;
import events.CreatureAttackEvent;
import events.TurnEndEvent;
import server.websocket.CommsWebSocket;

/**
 * Class to represent a game.
 *
 * @author Raghu
 *
 */
public class Game implements Jsonifiable {
	private static final int			PLAYER_START_LIFE	= 30;
	private Board									board;
	private Player								playerOne;
	private Player								playerTwo;
	private int										id;
	private static AtomicInteger	idGenerator				= new AtomicInteger(0);

	public Game(List<String> firstPlayerCards, List<String> secondPlayerCards, int playerOneId, int playerTwoId) {
		// Initialize both players with starting life.
		playerOne = new Player(PLAYER_START_LIFE, PlayerType.PLAYER_ONE, playerOneId);
		playerTwo = new Player(PLAYER_START_LIFE, PlayerType.PLAYER_TWO, playerTwoId);

		// set game id.
		this.id = idGenerator.incrementAndGet();

		// build decks from the lists of cards.
		OrderedCardCollection deckOne = new OrderedCardCollection(Zone.DECK, playerOne);
		OrderedCardCollection deckTwo = new OrderedCardCollection(Zone.DECK, playerTwo);

		// format the card names from the lists.
		firstPlayerCards.stream().map(elt -> formatName(elt)).collect(Collectors.toList());
		secondPlayerCards.stream().map(elt -> formatName(elt)).collect(Collectors.toList());

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
			for (String formattedName : firstPlayerCards) {
				Object p = Class.forName(formattedName).getConstructor(Player.class).newInstance(playerOne);
				if (p instanceof Creature) {
					fCards.add((Creature) p);
				} else if (p instanceof AuraCard) {
					fCards.add((AuraCard) p);
				} else if (p instanceof SpellCard) {
					fCards.add((SpellCard) p);
				} else {
					throw new RuntimeException(
							"ERROR: Some sort of invalid card was trying to be added to Player One's deck. Name is : "
									+ formattedName);
				}
			}

			// repeat process with player two's deck.
			for (String formattedName : secondPlayerCards) {
				Object p = Class.forName(formattedName).getConstructor(Player.class).newInstance(playerTwo);
				if (p instanceof Creature) {
					sCards.add((Creature) p);
				} else if (p instanceof AuraCard) {
					sCards.add((AuraCard) p);
				} else if (p instanceof SpellCard) {
					sCards.add((SpellCard) p);
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
	}

	/**
	 * Takes in a card's name and produces a properly formatted name such that it
	 * matches the card's class name (so we can use reflection).
	 *
	 * @param name
	 *          the name of the card in question.
	 * @return the string that matches the card's class (based on our naming
	 *         conventions).
	 */
	private String formatName(String name) {
		// all non alphanumeric characters replaced with empty string, including
		// spaces.
		return name.replaceAll("[^A-Za-z0-9]", "");
	}

	public void setBoard(Board board) {
		this.board = board;
	}

	public void startGame() {
		while (playerOne.getLife() > 0 && playerTwo.getLife() > 0) {
			// if neither player has 0 life the game goes on.

		}
		if (playerOne.getLife() <= 0 && playerTwo.getLife() > 0) {
			System.out.println("Player One loses.");
		} else if (playerTwo.getLife() <= 0 && playerOne.getLife() > 0) {
			System.out.println("Player Two loses.");
		} else {
			System.out.println("Game is drawn.");
		}
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
	 * Checks to see if user input is a valid action on the board. If so, we
	 * perform the user input.
	 *
	 * @param userInput
	 *          a JsonObject formatted according to the spec for user input.
	 */
	public void handleUserInput(JsonObject userInput) {
		int playerId = userInput.get("player").getAsInt();
		if (playerId != board.getActivePlayer().getId()) {
			// player acting out of turn.
			CommsWebSocket.sendActionBad(playerId);
			return;
		}
		String action = userInput.get("name").getAsString();

		// if it's a turnend action end the turn.
		if (action.equals("turnend")) {
			TurnEndEvent event = new TurnEndEvent(board.getActivePlayer());
			CommsWebSocket.sendActionOk(playerId);
			board.takeAction(event);
			return;
		}

		// since it's not a turnend they must have sent us a card.
		Card card = board.getCardById(userInput.get("IID1").getAsInt());
		// if user can't pay the card cost the action is bad.
		if (!(card.getOwner().validateCost(card.getCost()))) {
			CommsWebSocket.sendActionBad(playerId);
			return;
		}

		switch (action) {
		case "attacked":
			Card target = board.getCardById(userInput.get("IID2").getAsInt());
			if (target.getOwner().getId() == playerId) {
				// can't attack own card.
				CommsWebSocket.sendActionBad(playerId);
				return;
			}
			if (card instanceof Creature && target instanceof Creature) {
				Creature attacker = (Creature) card;
				Creature victim = (Creature) target;
				if (attacker.getNumAttacks() <= 0) {
					// the attacking creature can no longer attack.
					CommsWebSocket.sendActionBad(playerId);
					return;
				}
				CreatureAttackEvent event = new CreatureAttackEvent(attacker, victim);
				CommsWebSocket.sendActionOk(playerId);
				board.takeAction(event);
				return;
			} else {
				// if one of the cards isn't a creature it doesn't work.
				CommsWebSocket.sendActionBad(playerId);
				return;
			}
		case "targeted":
			break;
		case "played":
			// in this case the owner can indeed pay the card's cost.
			if (card instanceof TargetsOtherCard) {
				// in this case the user needed to select a target but they didn't.
				JsonObject inputRequest = new JsonObject();

			} else {
				// if they can indeed play the card we should play it!
			}
			break;
		}
		throw new IllegalArgumentException("ERROR: invalid name " + "passed from user to verifyUserInput");

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
