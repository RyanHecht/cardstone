package effects;

import java.util.function.Function;

import cardgamelibrary.Board;
import cardgamelibrary.Card;
import cardgamelibrary.Effect;

public class GateEffect implements Effect{

	Function<Board,Boolean> gate;
	boolean shouldContinue;
	
	private Card src;

	@Override
	public Card getSrc() {
		return src;
	}
	
	public GateEffect(Function<Board,Boolean> func, Card src){
		this.gate = func;
		shouldContinue = true;
		this.src = src;
	}
	
	@Override
	public void apply(Board board) {
		this.shouldContinue = (boolean) gate.apply(board);
	}
	
	public boolean getShouldContinue(){
		return shouldContinue;
	}

	@Override
	public EffectType getType() {
		return EffectType.GATE_EFFECT;
	}


}
