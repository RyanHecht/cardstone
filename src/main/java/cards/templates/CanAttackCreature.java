package cards.templates;

import cardgamelibrary.Card;
import cardgamelibrary.Effect;
import cardgamelibrary.Zone;
import effects.EmptyEffect;
import game.Player;

public interface CanAttackCreature extends Card{

	default public Effect onTurnStart(){
		return EmptyEffect.create();
	}
}
