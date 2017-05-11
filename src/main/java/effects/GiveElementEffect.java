package effects;

import cardgamelibrary.Board;
import cardgamelibrary.Card;
import cardgamelibrary.Effect;
import cardgamelibrary.ElementType;
import game.Player;

public class GiveElementEffect implements Effect {

	Player owner;
	ElementType element;
	int amount;
	
	@Override
	public void apply(Board board) {
		board.givePlayerElement(owner, element, amount,src);
	}

	public Player getOwner() {
		return owner;
	}

	public void setOwner(Player owner) {
		this.owner = owner;
	}

	public ElementType getElement() {
		return element;
	}

	public void setElement(ElementType element) {
		this.element = element;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	private Card src;

	@Override
	public Card getSrc() {
		return src;
	}
	
	public GiveElementEffect(Player owner, ElementType element, int amount, Card src) {
		super();
		this.owner = owner;
		this.element = element;
		this.amount = amount;
		this.src = src;
	}

	@Override
	public EffectType getType() {
		return EffectType.ELEMENT_GAINED;
	}

}
