package effects;

import cardgamelibrary.Board;
import cardgamelibrary.Card;
import cardgamelibrary.Effect;
import cardgamelibrary.Zone;

public class TransformEffect implements Effect {

	private Card target;
	private Card replaceWith;
	private Zone targetZone;

	private Card src;

	@Override
	public Card getSrc() {
		return src;
	}
	
	public TransformEffect(Card target, Card replaceWith, Zone targetZone, Card src) {
		super();
		this.target = target;
		this.replaceWith = replaceWith;
		this.targetZone = targetZone;
		this.src = src;
	}

	@Override
	public void apply(Board board) {
		board.transformCard(target, replaceWith, targetZone);
	}

	@Override
	public EffectType getType() {
		return EffectType.TRANSFORM;
	}

}
