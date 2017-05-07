package cards;

import cardgamelibrary.AuraCard;
import cardgamelibrary.CardType;
import cardgamelibrary.Effect;
import cardgamelibrary.ManaPool;
import cardgamelibrary.Zone;
import effects.EffectType;
import effects.PlayerDamageEffect;
import game.Player;

public class StoneHide extends AuraCard {

	private static final ManaPool defaultCost = new ManaPool(20, 0, 0, 2, 0, 0);
	private static final String defaultImage = "images/StoneHide.jpg";
	private static final String defaultName = "Stone Hide";
	private static final String defaultText = "Reduce all instances of damage you take by 1";
	private static final CardType defaultType = CardType.AURA;

	public StoneHide(Player owner) {
		super(defaultCost, defaultImage, owner, defaultName, defaultText, defaultType);
	}

	public boolean onProposedEffect(Effect e, Zone z) {
		if (z != Zone.AURA_BOARD) {
			return false;
		}

		if (e.getType() == EffectType.PLAYER_DAMAGED) {
			PlayerDamageEffect pde = (PlayerDamageEffect) e;
			if (pde.getPlayerDamaged().equals(this.getOwner())) {
				if (pde.getDmg() > 0) {
					return true;
				}
			}
		}
		return false;
	}

	public Effect getNewProposition(Effect e, Zone z) {
		PlayerDamageEffect old = (PlayerDamageEffect) e;
		PlayerDamageEffect newEffect;
		newEffect = new PlayerDamageEffect(old.getPlayerDamaged(), old.getSource(), old.getDmg() - 1);
		return newEffect;
	}
}
