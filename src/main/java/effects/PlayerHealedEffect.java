package effects;

import cardgamelibrary.Board;
import cardgamelibrary.Card;
import cardgamelibrary.Effect;
import game.Player;

public class PlayerHealedEffect implements Effect {

	private Card		source;
	private Player	playerHealed;
	private int			heal;

	public PlayerHealedEffect(Card c, Player target, int heal) {
		source = c;
		playerHealed = target;
		this.heal = heal;
	}

	@Override
	public void apply(Board board) {
		board.healPlayer(playerHealed, source, heal);
	}

	@Override
	public EffectType getType() {
		return EffectType.PLAYER_HEALED;
	}

}