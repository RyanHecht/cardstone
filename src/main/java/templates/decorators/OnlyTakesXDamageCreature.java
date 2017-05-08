package templates.decorators;

import cardgamelibrary.CreatureInterface;
import cardgamelibrary.Effect;
import cardgamelibrary.Zone;
import effects.CardDamageEffect;
import effects.EffectType;

public class OnlyTakesXDamageCreature extends CreatureWrapper {

	private int allowedDamage;

	public OnlyTakesXDamageCreature(CreatureInterface c, int dmg) {
		super(c);
		allowedDamage = dmg;
	}

	@Override
	public boolean onProposedEffect(Effect e, Zone z) {
		if (e.getType() == EffectType.CARD_DAMAGED && z == Zone.CREATURE_BOARD) {
			CardDamageEffect cd = (CardDamageEffect) e;
			if (cd.getTarget().equals(this)) {
				return cd.getDamage() > allowedDamage;
			}
		}
		return true;
	}

}
