package effects;

import cardgamelibrary.Board;
import cardgamelibrary.Card;
import cardgamelibrary.Effect;
import game.Player;

public class PlayerHealedEffect implements Effect {

	private Card		source;
	private Player	playerHealed;
	public Card getSource() {
		return source;
	}

	public void setSource(Card source) {
		this.source = source;
	}

	public Player getPlayerHealed() {
		return playerHealed;
	}

	public void setPlayerHealed(Player playerHealed) {
		this.playerHealed = playerHealed;
	}

	public int getHeal() {
		return heal;
	}

	public void setHeal(int heal) {
		this.heal = heal;
	}

	private int			heal;

	public PlayerHealedEffect(Card c, Player target, int heal) {
		source = c;
		playerHealed = target;
		this.heal = heal;
	}

	@Override
	public void apply(Board board) {
		System.out.println("I tryna heay");
		board.healPlayer(playerHealed, source, heal);
	}

	@Override
	public EffectType getType() {
		return EffectType.PLAYER_HEALED;
	}

	@Override
	public Card getSrc() {
		return source;
	}

}