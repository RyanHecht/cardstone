package cardgamelibrary;

import java.io.Serializable;

/**
 * Represents the notion of a location in the game.
 *
 * @author 42jpa
 *
 */
public enum Zone implements Serializable {

	HAND, DECK, CREATURE_BOARD, AURA_BOARD, EXILE, GRAVE;

}
