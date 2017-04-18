package effects;

import cardgamelibrary.Board;
import cardgamelibrary.Card;
import cardgamelibrary.Creature;
import cardgamelibrary.Effect;

public class CardDamageEffect implements Effect {

	private Card			source;
	private Creature	cardDamaged;
	private int				dmg;

	public CardDamageEffect(Card c, Creature target, int dmg) {
		source = c;
		cardDamaged = target;
		this.dmg = dmg;
	}

	@Override
	public void apply(Board board) {
		// do we even have to search for the creature?
		cardDamaged.takeDamage(dmg, source);
	}

}
