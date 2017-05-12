package effects;

import cardgamelibrary.Board;
import cardgamelibrary.Card;
import cardgamelibrary.CreatureInterface;
import cardgamelibrary.Effect;

public class KillCreatureEffect implements Effect {

	Card src;
	CreatureInterface target;
	
	
	
	public KillCreatureEffect(Card src, CreatureInterface target) {
		super();
		this.src = src;
		this.target = target;
	}

	@Override
	public void apply(Board board) {
		board.creatureDies(target);
	}

	@Override
	public EffectType getType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Card getSrc() {
		// TODO Auto-generated method stub
		return null;
	}

}
