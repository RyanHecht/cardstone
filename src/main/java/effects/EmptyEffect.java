package effects;

import cardgamelibrary.Board;
import cardgamelibrary.Effect;

/**
 * Class to represent an effect that does nothing. It's final so people can't
 * subclass it and screw around b/c that could cause major issues. Get instances
 * by using the "create" method that will return an effect. It's a singleton
 * since we don't need multiple copies of this.
 *
 * @author Raghu
 *
 */
public final class EmptyEffect implements Effect {
	private static final EmptyEffect effect = new EmptyEffect();

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
		return effect;
	}

}
