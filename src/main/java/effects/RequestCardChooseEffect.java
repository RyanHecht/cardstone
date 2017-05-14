package effects;

import java.util.List;

import cardgamelibrary.Board;
import cardgamelibrary.Card;
import cardgamelibrary.Effect;
import game.Player;

public class RequestCardChooseEffect implements Effect{	
	List<Card> options;
	Player player;
	Card chooser;
	Card src;
	public RequestCardChooseEffect(List<Card> options, Player player, Card chooser, Card src) {
		super();
		this.options = options;
		this.player = player;
		this.chooser = chooser;
		this.src = src;
	}
	
	@Override
	public void apply(Board board) {
		board.requestChoose(options, player, chooser);
	}
	@Override
	public EffectType getType() {
		return EffectType.REQUEST_CARD_CHOICE;
	}
	@Override
	public Card getSrc() {
		return src;
	}
	
	
	
}
