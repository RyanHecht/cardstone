package cards;

import cardgamelibrary.Card;
import cardgamelibrary.CardType;
import cardgamelibrary.ConcatEffect;
import cardgamelibrary.CreatureInterface;
import cardgamelibrary.Effect;
import cardgamelibrary.ManaPool;
import cardgamelibrary.SpellCard;
import cardgamelibrary.Zone;
import devotions.AirDevotion;
import effects.AddToOccEffect;
import effects.CardDamageEffect;
import effects.PlayerDamageEffect;
import effects.RandomCreatureDamage;
import game.Player;
import templates.TargetsOtherCard;
import templates.TargetsPlayer;

public class AirSlash extends SpellCard implements TargetsOtherCard, TargetsPlayer{

	private static final String defaultImage = "images/AirSlash.png";
	private static final String defaultName = "Air Slash";
	private static final String defaultText = "Return the targeted enemy minion to your opponent's hand. If you have 2 storm, deal 3 damage to 2 random enemy minions. If you have 4 storm, deal 5 damage to the enemy hero.";
	private static final CardType defaultType = CardType.SPELL;

	public AirSlash(Player owner) {
		super(new ManaPool(40, 0, 0, 0, 1, 0), defaultImage, owner, defaultName, defaultText, defaultType);
	}

	@Override
	public boolean playerValidTarget(Player p) {
		return false;
	}

	@Override
	public boolean cardValidTarget(Card card, Zone targetIn) {
		if (card.isA(CreatureInterface.class) && targetIn == Zone.CREATURE_BOARD && card.getOwner() != getOwner()) {
			return true;
		}
		return false;
	}

	@Override
	public Effect impactCardTarget(Card target, Zone targetIn) {
		ConcatEffect ce = new ConcatEffect(this);
		ce.addEffect(new AddToOccEffect(target,target.getOwner(),targetIn,Zone.HAND,this));
		if(AirDevotion.getLevelOfAir(getOwner().getDevotion()) >= 2){
			for(int x = 0; x < 2; x++){
				ce.addEffect(new RandomCreatureDamage(false,true,3,this));
			}
		}
		if(AirDevotion.getLevelOfAir(getOwner().getDevotion()) >= 4){
			ce.addEffect(new PlayerDamageEffect(target.getOwner(),this,5));
		}
		return ce;
	}
	
}
