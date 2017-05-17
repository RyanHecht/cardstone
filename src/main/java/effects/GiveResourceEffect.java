package effects;

import cardgamelibrary.Board;
import cardgamelibrary.Card;
import cardgamelibrary.Effect;
import cardgamelibrary.ElementType;
import game.Player;

public class GiveResourceEffect implements Effect{

	Player owner;
	int amount;
	
	@Override
	public void apply(Board board) {
		board.givePlayerRes(owner, amount,src);
	}

	public Player getOwner() {
		return owner;
	}

	public void setOwner(Player owner) {
		this.owner = owner;
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
	
	public GiveResourceEffect(Player owner, int amount, Card src) {
		super();
		this.owner = owner;
		this.amount = amount;
		this.src = src;
	}

	@Override
	public EffectType getType() {
		return EffectType.ELEMENT_GAINED;
	}
	
}
