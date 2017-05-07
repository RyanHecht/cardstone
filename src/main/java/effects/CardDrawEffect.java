package effects;

import cardgamelibrary.Board;
import cardgamelibrary.Card;
import cardgamelibrary.Effect;
import game.Player;

public class CardDrawEffect implements Effect{

	private Player target;
	private Card src;

	public CardDrawEffect(Player target, Card src){
		this.target = target;
		this.src = src;
		
	}
	
	@Override
	public void apply(Board board) {
		board.drawCard(target);
	}

	@Override
	public EffectType getType() {
		return EffectType.CARD_DRAWN;
	}

	@Override
	public Card getSrc() {
		return src;
	}

}
