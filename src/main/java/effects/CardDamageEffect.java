package effects;

import cardgamelibrary.Board;
import cardgamelibrary.Card;
import cardgamelibrary.CreatureInterface;

public class CardDamageEffect implements DamageInterface {

	private Card source;
	private CreatureInterface cardDamaged;
	private int dmg;

	public CardDamageEffect(Card c, CreatureInterface target, int dmg) {
		source = c;
		cardDamaged = target;
		this.dmg = dmg;
	}

	public EffectType getType() {
		return EffectType.CARD_DAMAGED;
	}

	@Override
	public void apply(Board board) {
		board.damageCard(cardDamaged, source, dmg);
	}
	
	public CreatureInterface getTarget(){
		return cardDamaged;
	}
	
	public int getDmg(){
		return dmg;
	}
	
	

	@Override
	public Card getSrc() {
		return source;
	}

	@Override
	public void setDamage(int dmg) {
		this.dmg = dmg;
	}

	@Override
	public int getDamage() {
		return dmg;
	}

}
