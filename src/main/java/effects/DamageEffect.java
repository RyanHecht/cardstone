package effects;

import cardgamelibrary.Board;
import cardgamelibrary.Card;
import cardgamelibrary.Creature;
import cardgamelibrary.Effect;

public class DamageEffect implements Effect {

	private Card			source;
	private Creature	cardDamaged;
	private int				dmg;

	public DamageEffect(Card c, Creature target, int dmg) {
		source = c;
		cardDamaged = target;
		this.dmg = dmg;
	}

	@Override
	public void apply(Board board) {
		// TODO Auto-generated method stub
		for (Creature cr : board.getOnBoard()) {
			if (cr.equals(cardDamaged)) {
				cr.takeDamage(dmg, source);
			}

		}
	}

}
