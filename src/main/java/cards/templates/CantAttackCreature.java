package cards.templates;

import cardgamelibrary.Card;
import cardgamelibrary.Effect;
import cardgamelibrary.Zone;
import effects.EmptyEffect;
import game.Player;

public interface CantAttackCreature extends Card {

	@Override
	default public Effect onTurnStart(Player p, Zone z) {
		return EmptyEffect.create();
	}

}
