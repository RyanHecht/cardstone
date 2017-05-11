package effects;

import cardgamelibrary.Board;
import cardgamelibrary.Card;
import cardgamelibrary.Effect;
import cardgamelibrary.Element;
import cardgamelibrary.ElementType;
import game.Player;

public class ApplyDevotionEffect implements Effect{

	private Player target;
	private Card src;
	private ElementType type;

	public ApplyDevotionEffect(Player target, ElementType type,Card src){
		this.target = target;
		this.src = src;
		this.type = type;
	}
	
	@Override
	public void apply(Board board) {
		board.applyDevotion(target, type, src);
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
