package cardgamelibrary;

import java.util.LinkedList;

public class ConcatEffect implements Effect{

	private LinkedList<Effect> effects;

	public ConcatEffect(){
		this.effects = new LinkedList<Effect>();
	}
	
	public void addEffect(Effect e){
		effects.add(e);
	}
	
	@Override
	public void apply(Board board) {
		if(this.effects.size() > 0){
			Effect e = effects.pop();
			e.apply(board);
		}
	}
	
	public boolean hasNext(){
		return effects.size() > 0;
	}

	
}
