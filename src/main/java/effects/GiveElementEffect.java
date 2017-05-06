package effects;

import cardgamelibrary.Board;
import cardgamelibrary.Effect;
import cardgamelibrary.ElementType;
import game.Player;

public class GiveElementEffect implements Effect {

	Player owner;
	ElementType element;
	int amount;
	
	@Override
	public void apply(Board board) {
		board.givePlayerElement(owner, element, amount);
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

	public GiveElementEffect(Player owner, ElementType element, int amount) {
		super();
		this.owner = owner;
		this.element = element;
		this.amount = amount;
	}

	@Override
	public EffectType getType() {
		return EffectType.ELEMENT_GAINED;
	}

}
