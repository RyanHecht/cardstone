package effects;

import cardgamelibrary.Board;
import cardgamelibrary.Card;
import cardgamelibrary.Effect;

public class ApplyEffect implements Effect {

	private Card target;
	private Card replaceWith;
	
	public Card getTarget() {
		return target;
	}

	public void setTarget(Card target) {
		this.target = target;
	}

	public Card getReplaceWith() {
		return replaceWith;
	}

	public void setReplaceWith(Card replaceWith) {
		this.replaceWith = replaceWith;
	}

	public ApplyEffect(Card target, Card replaceWith) {
		super();
		this.target = target;
		this.replaceWith = replaceWith;
	}

	@Override
	public void apply(Board board) {
		board.applyToCard(target, replaceWith);
	}

	@Override
	public EffectType getType() {
		return EffectType.CARD_APPLIED_TO;
	}

}
