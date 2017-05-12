package effects;

import cardgamelibrary.Board;
import cardgamelibrary.Card;
import cardgamelibrary.CreatureInterface;
import cardgamelibrary.Effect;

public class DeathNonPersistEffect implements Effect {

	private CreatureInterface target;
	
	public DeathNonPersistEffect(CreatureInterface creature) {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void apply(Board board) {
		board.deathNonPersist(target);
	}

	@Override
	public EffectType getType() {
		return EffectType.DEATH_NON_PERSIST;
	}

	@Override
	public Card getSrc() {
		return target;
	}

}
