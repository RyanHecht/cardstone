package effects;

import cardgamelibrary.Board;
import cardgamelibrary.Card;
import cardgamelibrary.Zone;
import game.Player;

public class AddToOccCrossPlayerEffect extends AddToOccEffect{

	private Player target;

	public AddToOccCrossPlayerEffect(Card card, Player owner, Player target, Zone end, Zone start, Card src) {
		super(card, owner, end, start, src);
		this.target = target;
	}

	public void apply(Board board){
		card.setOwner(target);
		board.addCardToOcc(card, board.getOcc(owner, end), board.getOcc(target, start));
	}

}
