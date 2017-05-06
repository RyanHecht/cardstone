package effects;

import cardgamelibrary.Board;
import cardgamelibrary.Effect;

public class CancelledEffect implements Effect {

	@Override
	public void apply(Board board) {
		// TODO Auto-generated method stub

	}

	@Override
	public EffectType getType() {
		return EffectType.EFFECT_CANCELLED;
	}

}
