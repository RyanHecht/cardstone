package effects;

import cardgamelibrary.Board;
import cardgamelibrary.Card;
import cardgamelibrary.Effect;
import cards.CheapJoshCreature;
import game.Player;
import game.PlayerType;

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
	private static final Card src = new CheapJoshCreature(new Player(-1100,PlayerType.PLAYER_ONE,-1100));
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

	@Override
	public EffectType getType() {
		return EffectType.EMPTY;
	}

	@Override
	public Card getSrc() {
		return src;
	}

}
