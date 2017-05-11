package cards;

import cardgamelibrary.AuraCard;
import cardgamelibrary.Card;
import cardgamelibrary.CardType;
import cardgamelibrary.ConcatEffect;
import cardgamelibrary.Effect;
import cardgamelibrary.ManaPool;
import cardgamelibrary.Zone;
import devotions.EarthDevotion;
import effects.AddToOccEffect;
import effects.CancelledEffect;
import effects.EffectType;
import effects.EmptyEffect;
import effects.PlayerDamageEffect;
import game.Player;

public class ProtectedBySand extends AuraCard {
	private int instancesToPrevent;

	public ProtectedBySand(Player owner) {
		super(new ManaPool(15, 0, 0, 1, 0, 0), defaultImage, owner, defaultName, defaultText, defaultType);
		instancesToPrevent = 0;
	}

	private static final String defaultImage = "images/ProtectedBySand.png";
	private static final String defaultName = "Protected By Sand";
	private static final String defaultText = "Aura. Prevent the next (sleeping stone) instances of damage you would take.";
	private static final CardType defaultType = CardType.SPELL;

	public boolean onProposedEffect(Effect e, Zone z) {
		if (z == Zone.AURA_BOARD) {
			if (this.instancesToPrevent > 0) {
				if (e.getType() == EffectType.PLAYER_DAMAGED) {
					PlayerDamageEffect pde = (PlayerDamageEffect) e;
					if (pde.getPlayerDamaged() == this.getOwner()) {
						return true;
					}
				}
			}
		}
		return false;
	}

	@Override
	public Effect getNewProposition(Effect e, Zone z) {
		if (onProposedEffect(e, z)) {
			instancesToPrevent--;
			ConcatEffect ce = new ConcatEffect(this);
			ce.addEffect(new CancelledEffect(this));
			if (instancesToPrevent <= 0) {
				ce.addEffect(new AddToOccEffect(this, getOwner(), Zone.GRAVE, Zone.AURA_BOARD, this));
			}
			return ce;
		}
		return EmptyEffect.create();
	}

	public Effect onThisPlayed(Card c, Zone z) {
		instancesToPrevent = EarthDevotion.getLevelOfEarth(getOwner().getDevotion());
		return EmptyEffect.create();
	}

}
