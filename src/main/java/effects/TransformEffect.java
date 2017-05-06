package effects;

import cardgamelibrary.Board;
import cardgamelibrary.Card;
import cardgamelibrary.Effect;
import cardgamelibrary.Zone;

public class TransformEffect implements Effect {

	private Card target;
	private Card replaceWith;
	private Zone targetZone;

	public TransformEffect(Card target, Card replaceWith, Zone targetZone) {
		super();
		this.target = target;
		this.replaceWith = replaceWith;
		this.targetZone = targetZone;
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
