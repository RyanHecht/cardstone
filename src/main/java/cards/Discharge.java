package cards;

import cardgamelibrary.Card;
import cardgamelibrary.CardType;
import cardgamelibrary.ConcatEffect;
import cardgamelibrary.Effect;
import cardgamelibrary.ManaPool;
import cardgamelibrary.SpellCard;
import cardgamelibrary.SpellInterface;
import cardgamelibrary.Zone;
import devotions.AirDevotion;
import effects.AddToOccEffect;
import effects.AoeDamageEffect;
import effects.EmptyEffect;
import game.Player;
import lambda.FunctionThreeMaker;

public class Discharge extends SpellCard {
	private static final String defaultImage = "images/Discharge.jpg";
	private static final String defaultName = "Discharge";
	private static final String defaultText = "Deal your storm charge level / 2 to all enemy minions.";
	private static final CardType defaultType = CardType.SPELL;
	private int numSpellsPlayed = 0;

	public Discharge(Player owner) {
		super(new ManaPool(30, 0, 0, 0, 2, 0), defaultImage, owner, defaultName, defaultText, defaultType);
	}

	public Effect onThisPlayed(Card c, Zone z) {
		// concat effect to deal damage and then send the card to the grave.
		ConcatEffect ce = new ConcatEffect(this);

		AoeDamageEffect damageCreatures = new AoeDamageEffect(AirDevotion.getLevelOfAir(getOwner().getDevotion()) / 2,
				0, 0, false, false, this,
				FunctionThreeMaker.targetsOtherPlayerCreatures(getOwner()), FunctionThreeMaker.determineCreatureDamage(0));
		ce.addEffect(damageCreatures);
		ce.addEffect(new AddToOccEffect(this, getOwner(), Zone.GRAVE, Zone.HAND, this));
		return ce;
	}
}
