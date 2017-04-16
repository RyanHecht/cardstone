package effects;

import cardgamelibrary.Board;
import cardgamelibrary.Effect;

/**
 * Class to represent an effect that does nothing.
 *
 * @author Raghu
 *
 */
public class EmptyEffect implements Effect {

	private EmptyEffect() {

	}

	@Override
	public void apply(Board board) {
		// empty method b/c it doesn't do anything.
	}

	/**
	 * Static method used to create empty effects.
	 *
	 * @return an empty effect.
	 */
	public static EmptyEffect create() {
		return new EmptyEffect();
	}

}
