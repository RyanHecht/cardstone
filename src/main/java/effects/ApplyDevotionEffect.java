package effects;

import cardgamelibrary.Board;
import cardgamelibrary.Card;
import cardgamelibrary.Effect;
import cardgamelibrary.Element;
import game.Player;

public class ApplyDevotionEffect implements Effect{

	private Player target;
	private Element src;

	public ApplyDevotionEffect(Player target, Element src){
		this.target = target;
		this.src = src;
	}
	
	@Override
	public void apply(Board board) {
		board.applyDevotion(target, src);
	}

	@Override
	public EffectType getType() {
		return EffectType.SET_DEVOTION;
	}

	@Override
	public Card getSrc() {
		return src;
	}

}
