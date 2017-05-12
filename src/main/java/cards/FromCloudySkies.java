package cards;

import cardgamelibrary.Card;
import cardgamelibrary.CardType;
import cardgamelibrary.CreatureInterface;
import cardgamelibrary.Effect;
import cardgamelibrary.ManaPool;
import cardgamelibrary.SpellCard;
import cardgamelibrary.Zone;
import devotions.AirDevotion;
import effects.CardDamageEffect;
import effects.PlayerDamageEffect;
import game.Player;
import templates.TargetsOtherCard;
import templates.TargetsPlayer;

public class FromCloudySkies extends SpellCard  implements TargetsOtherCard, TargetsPlayer {

	private static final String		defaultImage	= "images/FromCloudySkies.jpg";
	private static final String		defaultName		= "From Cloudy Skies...";
	private static final String		defaultText		= "Deal 2. If you have at least 6 storm charge, deal 5 instead.";
	private static final CardType	defaultType		= CardType.SPELL;
	private boolean deal5;
	
	public FromCloudySkies(Player owner) {
		super(new ManaPool(15, 0, 0, 0, 1, 0), defaultImage, owner, defaultName, defaultText, defaultType);
	}

	@Override
	public boolean playerValidTarget(Player p) {
		return true;
	}

	@Override
	public boolean cardValidTarget(Card card, Zone targetIn) {
		return targetIn.equals(Zone.CREATURE_BOARD);
	}

	@Override
	public Effect impactCardTarget(Card target, Zone targetIn) {
		if(AirDevotion.getLevelOfAir(getOwner().getDevotion()) >= 6){
			return new CardDamageEffect(this,(CreatureInterface) target, 5);
		}
		return new CardDamageEffect(this,(CreatureInterface) target, 2);
	}
	
	@Override
	public Effect impactPlayerTarget(Player p) {
		if(AirDevotion.getLevelOfAir(getOwner().getDevotion()) >= 6){
			return new PlayerDamageEffect(p, this, 5);
		}
		return new PlayerDamageEffect(p, this, 2);
	}
	
}
