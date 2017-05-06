package effects;

import cardgamelibrary.Board;
import cardgamelibrary.Card;
import cardgamelibrary.Effect;
import game.Player;

public class PlayerDamageEffect implements Effect {

	private Card		source;
	private Player	playerDamaged;
	private int			dmg;

	public PlayerDamageEffect(Player target, Card c, int dmg) {
		source = c;
		playerDamaged = target;
		this.dmg = dmg;
	}

	@Override
	public void apply(Board board) {
		// TODO Auto-generated method stub
		board.damagePlayer(playerDamaged, source, dmg);
	}

	@Override
	public EffectType getType() {
		return EffectType.PLAYER_DAMAGED;
	}

}
