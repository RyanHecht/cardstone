package effects;

import java.util.function.Function;

import cardgamelibrary.Board;
import cardgamelibrary.Card;
import cardgamelibrary.Effect;

public class EffectMaker implements Effect{

	Function<Board,Effect> func;
	
	public EffectMaker(Function<Board,Effect> func, Card src){
		this.func = func;
		this.src = src;
		
	}
	
	private Card src;

	@Override
	public Card getSrc() {
		return src;
	}
	
	public void apply(Board board){
		
	}
	
	public Effect getEffect(Board board){
		return func.apply(board);
	}
	
	public EffectType getType(){
		return EffectType.MAKER;
	}
	
}
