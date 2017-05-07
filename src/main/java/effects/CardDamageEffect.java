package effects;

import cardgamelibrary.Board;
import cardgamelibrary.Card;
import cardgamelibrary.CreatureInterface;
import cardgamelibrary.Effect;

public class CardDamageEffect implements Effect {

	private Card source;
	private CreatureInterface cardDamaged;
	private int dmg;
	

	public CardDamageEffect(Card c, CreatureInterface target, int dmg) {
		source = c;
		cardDamaged = target;
		this.dmg = dmg;
	}
	
	public EffectType getType(){
		return EffectType.CARD_DAMAGED;
	}

	@Override
	public void apply(Board board) {
		board.damageCard(cardDamaged, source, dmg);
	}

	@Override
	public Card getSrc() {
		return source;
	}

}
