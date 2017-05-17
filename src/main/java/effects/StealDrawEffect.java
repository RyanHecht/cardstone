package effects;

import cardgamelibrary.Board;
import cardgamelibrary.Card;
import cardgamelibrary.Effect;
import cardgamelibrary.Zone;
import game.Player;

public class StealDrawEffect extends CardDrawEffect implements Effect {

	public StealDrawEffect(Player target, Card src) {
		super(target, src);
	}
	
	public void apply(Board board) {
		Player opponent = board.getOpposingPlayer(target);
		if(board.getOcc(opponent, Zone.DECK).size() > 0){
			Card c = board.getOcc(opponent, Zone.DECK).getFirstCard();
			c.setOwner(target);
			board.addCardToOcc(c, board.getOcc(opponent, Zone.DECK), board.getOcc(target, Zone.HAND));
		}
	}

}
