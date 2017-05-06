package effects;

import java.util.function.Function;

import cardgamelibrary.Board;
import cardgamelibrary.Effect;

public class GateEffect implements Effect{

	Function<Board,Boolean> gate;
	boolean shouldContinue;
	
	public GateEffect(Function<Board,Boolean> func){
		this.gate = func;
		shouldContinue = true;
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
