package watercards;

import cardgamelibrary.AuraCard;
import cardgamelibrary.Card;
import cardgamelibrary.CardType;
import cardgamelibrary.CreatureInterface;
import cardgamelibrary.Effect;
import cardgamelibrary.ManaPool;
import cardgamelibrary.Zone;
import devotions.WaterDevotion;
import effects.ApplyEffect;
import effects.EmptyEffect;
import game.Player;
import templates.decorators.CantAttackForTurnsCreature;

public class WintersDomain extends AuraCard{

	private static final String defaultImage = "images/WintersDomain.jpg";
	private static final String defaultName = "Winter's Domain";
	private static final String defaultText = "Whenever an enemy minion attacks, freeze it.";
	private static final CardType defaultType = CardType.SPELL;
	public WintersDomain(Player owner) {
		super(new ManaPool(25, 0, 2, 0, 0, 0), defaultImage, owner, defaultName, defaultText, defaultType);
	}
	
	public Effect onCreatureAttack(CreatureInterface attacker, CreatureInterface target, Zone z){
		if(z.equals(Zone.AURA_BOARD) && !attacker.getOwner().equals(getOwner())){
			CantAttackForTurnsCreature caftc = new CantAttackForTurnsCreature((CreatureInterface) attacker,2);
			return new ApplyEffect(attacker,caftc,this);
		}
		return EmptyEffect.create();
	}
	
}
