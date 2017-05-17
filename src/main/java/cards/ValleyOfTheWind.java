package cards;

import cardgamelibrary.AuraCard;
import cardgamelibrary.CardType;
import cardgamelibrary.CreatureInterface;
import cardgamelibrary.Effect;
import cardgamelibrary.ElementType;
import cardgamelibrary.ManaPool;
import cardgamelibrary.Zone;
import effects.EmptyEffect;
import effects.SummonEffect;
import game.Player;

public class ValleyOfTheWind extends AuraCard{

	private static final String defaultImage = "images/ValleyOfTheWind.jpg";
	private static final String defaultName = "Valley of the Wind";
	private static final String defaultText = "Whenever a friendly air creature attacks, add an air element to your hand.";
	private static final CardType defaultType = CardType.AURA;

	public ValleyOfTheWind(Player owner) {
		super(new ManaPool(20, 0, 0, 0, 1, 0), defaultImage, owner, defaultName, defaultText, defaultType);
	}
	
	public Effect onCreatureAttack(CreatureInterface attacker, CreatureInterface target, Zone z) {
		if(attacker.getOwner().equals(getOwner()) && z.equals(Zone.AURA_BOARD) && attacker.hasElement(ElementType.AIR)){
			AirElement ae = new AirElement(getOwner());
			return new SummonEffect(ae,Zone.HAND,this);
		}
		return EmptyEffect.create();
	}
	
}
