package effects;

import cardgamelibrary.Board;
import cardgamelibrary.Card;
import cardgamelibrary.Effect;
import cardgamelibrary.ManaPool;
import game.Player;

public class PayCostEffect implements Effect {

	Card src;
	ManaPool cost;
	Player target;
	
	public PayCostEffect(Card src, ManaPool cost, Player target) {
		super();
		this.src = src;
		this.cost = cost;
		this.target = target;
	}

	@Override
	public void apply(Board board) {
		board.playerPayCost(target,cost);
	}

	@Override
	public EffectType getType() {
		return EffectType.PAY_COST;
	}

	@Override
	public Card getSrc() {
		return src;
	}

}
