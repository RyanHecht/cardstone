package cards;

import cardgamelibrary.Board;
import cardgamelibrary.Card;
import cardgamelibrary.CardType;
import cardgamelibrary.ConcatEffect;
import cardgamelibrary.CreatureInterface;
import cardgamelibrary.Effect;
import cardgamelibrary.ManaPool;
import cardgamelibrary.SpellCard;
import cardgamelibrary.Zone;
import devotions.EarthDevotion;
import effects.ApplyEffect;
import effects.CardDamageEffect;
import game.Player;
import templates.TargetsOtherCard;
import templates.decorators.CantAttackForTurnsCreature;

public class RockShock extends SpellCard implements TargetsOtherCard {

	public RockShock(Player owner) {
		super(new ManaPool(15, 0, 0, 2, 0, 0), defaultImage, owner, defaultName, defaultText, defaultType);
	}

	private static final String defaultImage = "images/RockShock.jpg";
	private static final String defaultName = "Rock Shock";
	private static final String defaultText = "Deal one damage to target minion and prevent it from attacking for a number of turns equal to your sleeping stone count.";
	private static final CardType defaultType = CardType.SPELL;

	public boolean cardValidTarget(Card card, Zone z) {
		if (card.getType() == CardType.CREATURE && z == Zone.CREATURE_BOARD) {
			return true;
		}
		return false;
	}


	public Effect impactCardTarget(Card target, Zone zone) {
		ConcatEffect ce = new ConcatEffect(this);
		ce.addEffect(new CardDamageEffect(this,(CreatureInterface)target,1));
		ce.addEffect(new ApplyEffect(target, 
				new CantAttackForTurnsCreature((CreatureInterface) target,
				2 * EarthDevotion.getLevelOfEarth(getOwner().getDevotion())),this));
		return ce;
	}

}
