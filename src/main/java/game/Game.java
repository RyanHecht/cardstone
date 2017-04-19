package game;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import com.google.gson.JsonObject;

import cardgamelibrary.Board;
import cardgamelibrary.Jsonifiable;
import cardgamelibrary.OrderedCardCollection;
import cardgamelibrary.PlayableCard;
import cardgamelibrary.Zone;
import server.MessageTypeEnum;

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

	public Game(List<String> firstPlayerCards, List<String> secondPlayerCards) {
		// Initialize both players with starting life.
		playerOne = new Player(PLAYER_START_LIFE, PlayerType.PLAYER_ONE);
		playerTwo = new Player(PLAYER_START_LIFE, PlayerType.PLAYER_TWO);

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
				PlayableCard p = (PlayableCard) Class.forName(formattedName).getConstructor(Player.class)
						.newInstance(playerOne);
				fCards.add(p);
			}

			for (String formattedName : secondPlayerCards) {
				PlayableCard p = (PlayableCard) Class.forName(formattedName).getConstructor(Player.class)
						.newInstance(playerTwo);
				sCards.add(p);
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

	@Override
	public JsonObject jsonifySelf() {
		JsonObject result = new JsonObject();
		result.addProperty("type", String.valueOf(MessageTypeEnum.BOARD_STATE));
		JsonObject payload = new JsonObject();
		payload.add("player1", playerOne.jsonifySelf());
		payload.add("player2", playerTwo.jsonifySelf());
		payload.add("board", board.jsonifySelf());
		result.add("payload", payload);
		return result;
	}

	@Override
	public JsonObject jsonifySelfChanged() {
		JsonObject result = new JsonObject();
		result.addProperty("type", String.valueOf(MessageTypeEnum.BOARD_STATE));
		JsonObject payload = new JsonObject();
		payload.add("player1", playerOne.jsonifySelf());
		payload.add("player2", playerTwo.jsonifySelf());
		payload.add("board", board.jsonifySelfChanged());
		return result;
	}
}
