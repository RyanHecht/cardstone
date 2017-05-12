package effects;

import cardgamelibrary.Board;
import cardgamelibrary.Card;
import cardgamelibrary.CreatureInterface;
import cardgamelibrary.Effect;
import events.CreatureAttackEvent;

public class CreatureAttackCreatureEffect implements Effect{

	private CreatureInterface target;
	private CreatureInterface attacker;

	public CreatureInterface getTarget() {
		return target;
	}

	public void setTarget(CreatureInterface target) {
		this.target = target;
	}

	public CreatureInterface getAttacker() {
		return attacker;
	}

	public void setAttacker(CreatureInterface attacker) {
		this.attacker = attacker;
	}

	@Override
	public void apply(Board board) {
		CreatureAttackEvent cae = new CreatureAttackEvent(target,attacker);
		board.takeAction(cae);
	}

	public CreatureAttackCreatureEffect(CreatureInterface target, CreatureInterface attacker) {
		super();
		this.target = target;
		this.attacker = attacker;
	}

	@Override
	public EffectType getType() {
		return EffectType.CREATURE_ATTACK_CREATURE;
	}

	@Override
	public Card getSrc() {
		return attacker;
	}

	
	
}
