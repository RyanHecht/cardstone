package game;

import java.util.List;

import com.google.gson.JsonObject;

import cardgamelibrary.Board;
import cardgamelibrary.Card;
import cardgamelibrary.Jsonifiable;
import cardgamelibrary.OrderedCardCollection;
import cardgamelibrary.Zone;
import server.MessageTypeEnum;

/**
 * Class to represent a game.
 *
 * @author Raghu
 *
 */
public class Game implements Jsonifiable {
	private Board		board;
	private Player	playerOne;
	private Player	playerTwo;

	public Game(List<Card> firstPlayerCards, List<Card> secondPlayerCards) {
		// Initialize both players @30 life.
		playerOne = new Player(30, PlayerType.PLAYER_ONE);
		playerTwo = new Player(30, PlayerType.PLAYER_TWO);

		// build decks from the lists of cards.
		OrderedCardCollection deckOne = new OrderedCardCollection(Zone.DECK, playerOne);
		OrderedCardCollection deckTwo = new OrderedCardCollection(Zone.DECK, playerTwo);
		deckOne.addAll(firstPlayerCards);
		deckTwo.addAll(secondPlayerCards);

		// but how do we register players for all the cards?

		// Some sort of board constructor goes here.
		board = new Board(deckOne, deckTwo);
	}
	
	public void setBoard(Board board){
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
