package watercards;

import cardgamelibrary.Card;
import cardgamelibrary.CardType;
import cardgamelibrary.Creature;
import cardgamelibrary.CreatureInterface;
import cardgamelibrary.Effect;
import cardgamelibrary.ManaPool;
import cardgamelibrary.Zone;
import devotions.TideLevel;
import devotions.WaterDevotion;
import effects.CardDamageEffect;
import effects.EmptyEffect;
import effects.PlayerDamageEffect;
import game.Player;
import templates.TargetsOtherCard;
import templates.TargetsPlayer;

public class WaterSerpent extends Creature implements TargetsOtherCard, TargetsPlayer {

	private static final int			defaultMaxHealth	= 5;
	private static final int			defaultAttack		= 5;
	private static final String		defaultImage			= "images/WaterSerpent.jpg";
	private static final String		defaultName			= "Water Serpent";
	private static final String		defaultText			= "High tide: deal 4 to target enemy. Rising tide: deal 2 to target enemy.";
	
	public WaterSerpent(Player owner) {
		super(defaultMaxHealth, defaultAttack, new ManaPool(50, 0, 2, 0, 0, 0), defaultImage, owner, defaultName, defaultText, CardType.CREATURE);
	}

	@Override
	public boolean playerValidTarget(Player p) {
		return true;
	}

	@Override
	public Effect impactPlayerTarget(Player target) {
		TideLevel tl = WaterDevotion.getLevelOfWater(getOwner().getDevotion());
		if(tl.equals(TideLevel.HIGH)){
			return new PlayerDamageEffect(target,this,4);
		}
		else if(tl.equals(TideLevel.RISING)){
			return new PlayerDamageEffect(target,this,2);
		}
		return EmptyEffect.create();
	}

	@Override
	public boolean cardValidTarget(Card card, Zone targetIn) {
		return targetIn.equals(Zone.CREATURE_BOARD);
	}

	@Override
	public Effect impactCardTarget(Card target, Zone targetIn) {
		TideLevel tl = WaterDevotion.getLevelOfWater(getOwner().getDevotion());
		if(tl.equals(TideLevel.HIGH)){
			return new CardDamageEffect(this,(CreatureInterface) target,4);
		}
		else if(tl.equals(TideLevel.RISING)){
			return new CardDamageEffect(this,(CreatureInterface) target,2);
		}
		return EmptyEffect.create();
	}
}
