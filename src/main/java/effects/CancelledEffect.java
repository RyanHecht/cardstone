package effects;

import cardgamelibrary.Board;
import cardgamelibrary.Card;
import cardgamelibrary.Effect;

public class CancelledEffect implements Effect {

	@Override
	public void apply(Board board) {
		// TODO Auto-generated method stub

	}

	public CancelledEffect(Card src){
		this.src = src;
	}
	
	private Card src;

	@Override
	public Card getSrc() {
		return src;
	}
	
	@Override
	public EffectType getType() {
		return EffectType.EFFECT_CANCELLED;
	}

}
