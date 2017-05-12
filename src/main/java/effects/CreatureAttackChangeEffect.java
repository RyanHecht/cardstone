package effects;

import cardgamelibrary.Board;
import cardgamelibrary.Card;
import cardgamelibrary.CreatureInterface;
import cardgamelibrary.Effect;
import cardgamelibrary.Zone;

public class CreatureAttackChangeEffect implements Effect{

	private int magnitude;
	private CreatureInterface target;
	private Card src;
	
	
	public CreatureAttackChangeEffect(int magnitude, CreatureInterface target, Card src) {
		super();
		this.magnitude = magnitude;
		this.target = target;
		this.src = src;
	}

	@Override
	public void apply(Board board) {
		board.changeCreatureAttack(target, magnitude);
	}

	@Override
	public EffectType getType() {
		return EffectType.ATTACK_CHANGE;
	}

	@Override
	public Card getSrc() {
		return src;
	}

}
