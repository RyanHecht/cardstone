package cardgamelibrary;

import java.io.Serializable;
import java.util.LinkedList;

public class ConcatEffect implements Effect, Serializable {

	private LinkedList<Effect> effects;

	public ConcatEffect() {
		this.effects = new LinkedList<Effect>();
	}

	public void addEffect(Effect e) {
		effects.add(e);
	}

	@Override
	public void apply(Board board) {
		while (this.effects.size() > 0) {
			Effect e = effects.pop();
			e.apply(board);
		}
	}

	public boolean hasNext() {
		return effects.size() > 0;
	}

}
