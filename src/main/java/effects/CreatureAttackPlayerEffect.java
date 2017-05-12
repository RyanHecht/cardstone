package effects;

import cardgamelibrary.Board;
import cardgamelibrary.Card;
import cardgamelibrary.CreatureInterface;
import cardgamelibrary.Effect;
import events.PlayerAttackEvent;
import game.Player;

public class CreatureAttackPlayerEffect implements Effect{
	
	private CreatureInterface attacker;
	private Player target;

	
	public CreatureAttackPlayerEffect(CreatureInterface attacker, Player target) {
		super();
		this.attacker = attacker;
		this.target = target;
	}

	
	
	public CreatureInterface getAttacker() {
		return attacker;
	}



	public void setAttacker(CreatureInterface attacker) {
		this.attacker = attacker;
	}



	public Player getTarget() {
		return target;
	}



	public void setTarget(Player target) {
		this.target = target;
	}



	@Override
	public void apply(Board board) {
		PlayerAttackEvent pae = new PlayerAttackEvent(target,attacker);
		board.takeAction(pae);
	}

	@Override
	public EffectType getType() {
		return EffectType.PLAYER_ATTACKED;
	}

	@Override
	public Card getSrc() {
		return attacker;
	}

	
	
}
