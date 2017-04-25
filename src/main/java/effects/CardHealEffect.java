package effects;

import cardgamelibrary.Board;
import cardgamelibrary.Card;
import cardgamelibrary.Creature;
import cardgamelibrary.Effect;

public class CardHealEffect implements Effect {

	private Card			source;
	private Creature	cardHealed;
	private int				heal;

	public CardHealEffect(Card c, Creature target, int heal) {
		source = c;
		cardHealed = target;
		this.heal = heal;
	}

	@Override
	public void apply(Board board) {
		board.healCard(cardHealed, source, heal);
	}

}