package cards;

import cardgamelibrary.AuraCard;
import cardgamelibrary.CardType;
import cardgamelibrary.DevotionType;
import cardgamelibrary.Effect;
import cardgamelibrary.ElementType;
import cardgamelibrary.ManaPool;
import cardgamelibrary.Zone;
import devotions.EarthDevotion;
import effects.EmptyEffect;
import effects.GiveElementEffect;
import game.Player;

public class Mine extends AuraCard {

	private static final String defaultImage = "images/Mine.jpg";
	private static final String defaultName = "Mine";
	private static final String defaultText = "Aura. At the start of your turns, gain 1 earth for every 2 sleeping stone, rounded up.";
	private static final CardType defaultType = CardType.AURA;

	public Mine(Player owner) {
		super(new ManaPool(10, 0, 0, 3, 0, 0), defaultImage, owner, defaultName, defaultText, defaultType);
	}

	public Effect onTurnStart(Player p, Zone z) {
		
		if (p.equals(getOwner()) && z == Zone.AURA_BOARD) {
			if(p.getDevotion().getDevotionType().equals(DevotionType.EARTH)){
				EarthDevotion ed = (EarthDevotion) p.getDevotion();
				return new GiveElementEffect(p, ElementType.EARTH, ((ed.getSleepingStoneCount() + 1) / 2),this);
			}
		}
		return EmptyEffect.create();
	}

}
