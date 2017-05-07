package effects;

import cardgamelibrary.Board;
import cardgamelibrary.Card;
import cardgamelibrary.CreatureInterface;
import cardgamelibrary.Effect;

public class CreatureHealthChangeEffect implements Effect {

	int amount;
	CreatureInterface target;

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public CreatureInterface getTarget() {
		return target;
	}

	public void setTarget(CreatureInterface target) {
		this.target = target;
	}

	public CreatureHealthChangeEffect(int amount, CreatureInterface target, Card src) {
		super();
		this.amount = amount;
		this.target = target;
		this.src = src;
	}
	
	private Card src;

	@Override
	public Card getSrc() {
		return src;
	}

	@Override
	public void apply(Board board) {
		// TODO Auto-generated method stub
		board.changeCreatureHealth(target, amount);
	}

	@Override
	public EffectType getType() {
		// TODO Auto-generated method stub
		return EffectType.HEALTH_CHANGE;
	}

}
