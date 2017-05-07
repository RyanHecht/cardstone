package templates;

import cardgamelibrary.Card;
import cardgamelibrary.CreatureInterface;
import cardgamelibrary.Effect;
import cardgamelibrary.Zone;
import game.Player;

public interface OnAnyAttackCard extends Card {

	@Override
	default public Effect onCreatureAttack(CreatureInterface attacker, CreatureInterface target, Zone z) {
		return this.onAnyAttack(attacker, z);
	}

	@Override
	public default Effect onPlayerAttack(CreatureInterface attacker, Player target, Zone z) {
		return this.onAnyAttack(attacker, z);
	}

	public Effect onAnyAttack(CreatureInterface attacker, Zone z);

}
