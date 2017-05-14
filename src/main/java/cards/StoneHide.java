package cards;

import cardgamelibrary.AuraCard;
import cardgamelibrary.Board;
import cardgamelibrary.CardType;
import cardgamelibrary.DevotionType;
import cardgamelibrary.Effect;
import cardgamelibrary.ManaPool;
import cardgamelibrary.Zone;
import devotions.Devotion;
import devotions.EarthDevotion;
import effects.EffectType;
import effects.PlayerDamageEffect;
import game.Player;

public class StoneHide extends AuraCard {

	private static final String defaultImage = "images/StoneHide.jpg";
	private static final String defaultName = "Stone Hide";
	private static final String defaultText = "Reduce all instances of damage you take by 1 for every 2 sleeping stone, rounded up.";
	private static final CardType defaultType = CardType.AURA;

	public StoneHide(Player owner) {
		super(new ManaPool(20, 0, 0, 2, 0, 0), defaultImage, owner, defaultName, defaultText, defaultType);
	}

	private int calcDamageReduc(){
		Devotion d = getOwner().getDevotion();
		return (EarthDevotion.getLevelOfEarth(d) + 1) / 2;
	}
	
	public boolean onProposedEffect(Effect e, Zone z, Board b) {
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
		newEffect = new PlayerDamageEffect(old.getPlayerDamaged(), old.getSource(), old.getDmg() - calcDamageReduc());
		return newEffect;
	}
}
