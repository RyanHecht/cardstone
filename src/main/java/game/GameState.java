package game;

import java.io.Serializable;

/**
 * Enum to represent states the game can be in (context is user inputs).
 *
 * @author Raghu
 *
 */
public enum GameState implements Serializable {
	IDLE, AWAITING_CHOICE, GAME_OVER,
}
