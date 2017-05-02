package cards.templates;

import cardgamelibrary.Card;
import cardgamelibrary.Creature;
import cardgamelibrary.Effect;
import cardgamelibrary.Zone;
import game.Player;

public interface OnAnyAttackCard extends Card{

	default public Effect onCreatureAttack(Creature attacker, Creature target, Zone z) {
		return this.onAnyAttack(attacker, z);
	}
	
	public default Effect onPlayerAttack(Creature attacker, Player target, Zone z) {
		return this.onAnyAttack(attacker,z);
	}

	public Effect onAnyAttack(Creature attacker, Zone z);
	
}
