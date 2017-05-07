package effects;

import cardgamelibrary.Board;
import cardgamelibrary.Card;
import game.Player;

public class PlayerDamageEffect implements DamageInterface {

	private Card source;

	public Card getSource() {
		return source;
	}

	public void setSource(Card source) {
		this.source = source;
	}

	public Player getPlayerDamaged() {
		return playerDamaged;
	}

	public void setPlayerDamaged(Player playerDamaged) {
		this.playerDamaged = playerDamaged;
	}

	public int getDmg() {
		return dmg;
	}

	public void setDmg(int dmg) {
		this.dmg = dmg;
	}

	private Player playerDamaged;
	private int dmg;

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

	@Override
	public Card getSrc() {
		return source;
	}

	@Override
	public void setDamage(int dmg) {
		this.dmg = dmg;
	}

	@Override
	public int getDamage() {
		return dmg;
	}

}
