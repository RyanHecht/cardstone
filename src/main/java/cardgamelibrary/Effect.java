package cardgamelibrary;

import effects.EmptyEffect;

/**
 * An effect which can be added to the queue, and executed when appropriate. Changes the board state in some way.
 * @author 42jpa
 *
 */
public interface Effect {

	/**
	 * Perform whatever the effect is on the board.
	 * @param board
	 */
	public void apply(Board board);

	
	public default boolean hasNext(){
		return false;
	}
	
}
