package effects;

import cardgamelibrary.Board;
import cardgamelibrary.Effect;

/**
 * Class to represent an effect that does nothing. It's final so people can't
 * subclass it and screw around b/c that could cause major issues.
 *
 * @author Raghu
 *
 */
public final class EmptyEffect implements Effect {

	/*
	 * no instantiation allowed.
	 */
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
