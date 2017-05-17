package cards;

import cardgamelibrary.AuraCard;
import cardgamelibrary.CardType;
import cardgamelibrary.ConcatEffect;
import cardgamelibrary.CreatureInterface;
import cardgamelibrary.Effect;
import cardgamelibrary.ElementType;
import cardgamelibrary.ManaPool;
import cardgamelibrary.Zone;
import effects.EmptyEffect;
import effects.RandomCreatureDamage;
import effects.SummonEffect;
import game.Player;

public class TheWindyForest extends AuraCard{

	private static final String defaultImage = "images/TheWindyForest.jpg";
	private static final String defaultName = "The Windy Forest";
	private static final String defaultText = "Whenever a friendly air creature attacks, also deal its attack as damage randomly split among enemies";
	private static final CardType defaultType = CardType.AURA;

	public TheWindyForest(Player owner) {
		super(new ManaPool(70, 0, 0, 0, 3, 0), defaultImage, owner, defaultName, defaultText, defaultType);
	}
	
	public Effect onCreatureAttack(CreatureInterface attacker, CreatureInterface target, Zone z) {
		if(attacker.getOwner().equals(getOwner()) && z.equals(Zone.AURA_BOARD) && attacker.hasElement(ElementType.AIR)){
			ConcatEffect ce = new ConcatEffect(this);
			int dmg = attacker.getAttack();
			for(int x = 0; x < dmg; x++){
				ce.addEffect(new RandomCreatureDamage(false,true,1,this));
			}
		}
		return EmptyEffect.create();
	}
	
}
