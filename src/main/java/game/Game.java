package game;

import com.google.gson.JsonObject;

import cardgamelibrary.Board;
import server.MessageTypeEnum;

/**
 * Class to represent a game.
 * 
 * @author Raghu
 *
 */
public class Game {
	private Board		board;
	private Player	playerOne;
	private Player	playerTwo;

	public Game() {
		// Initialize both players @30 life.
		playerOne = new Player(30);
		playerTwo = new Player(30);

		// Some sort of board constructor goes here.
		board = new Board();
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
	
	public JsonObject jsonifySelf(){
		JsonObject result = new JsonObject();
		result.addProperty("type", String.valueOf(MessageTypeEnum.BOARD_STATE));
		JsonObject payload = new JsonObject();
		payload.addProperty("p1Health", );
		payload.add("board", board.jsonifySelf());
		return result;
	}
}
