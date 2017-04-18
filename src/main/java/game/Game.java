package game;

import com.google.gson.JsonObject;

import cardgamelibrary.Board;
import cardgamelibrary.Jsonifiable;
import server.MessageTypeEnum;

/**
 * Class to represent a game.
 *
 * @author Raghu
 *
 */
public class Game implements Jsonifiable{
	private Board		board;
	private Player	playerOne;
	private Player	playerTwo;

	public Game() {
		// Initialize both players @30 life.
		playerOne = new Player(30, PlayerType.PLAYER_ONE);
		playerTwo = new Player(30, PlayerType.PLAYER_TWO);

		// Some sort of board constructor goes here.
		// board = new Board();
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
	
	public JsonObject jsonifySelfChanged(){
		JsonObject result = new JsonObject();
		result.addProperty("type", String.valueOf(MessageTypeEnum.BOARD_STATE));
		JsonObject payload = new JsonObject();
		payload.add("player1", playerOne.jsonifySelf());
		payload.add("player2", playerTwo.jsonifySelf());
		payload.add("board", board.jsonifySelfChanged());
		return result;
	}
}
