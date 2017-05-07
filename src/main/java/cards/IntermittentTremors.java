package cards;

import cardgamelibrary.AuraCard;
import cardgamelibrary.Card;
import cardgamelibrary.CardType;
import cardgamelibrary.Effect;
import cardgamelibrary.ManaPool;
import cardgamelibrary.Zone;
import effects.AoeDamageEffect;
import effects.EmptyEffect;
import game.Player;
import lambda.FunctionThreeMaker;

public class IntermittentTremors extends AuraCard {

	private static final ManaPool defaultCost = new ManaPool(15, 0, 0, 2, 0, 0);
	private static final String defaultImage = "images/IntermittentTremors.jpg";
	private static final String defaultName = "Intermittent Tremors";
	private static final String defaultText = "At the start of every other one of your turns, deal 1 to all minions";
	private static final CardType defaultType = CardType.AURA;
	private int turnsToTremor;

	public IntermittentTremors(Player owner) {
		super(defaultCost, defaultImage, owner, defaultName, defaultText, defaultType);
		turnsToTremor = 0;
	}

	public Effect onThisPlayed(Card c, Zone z) {
		turnsToTremor = 1;
		return EmptyEffect.create();
	}

	public Effect onTurnStart(Player p, Zone z) {
		if (z == Zone.AURA_BOARD) {
			if (turnsToTremor == 0) {
				turnsToTremor = 3;

				return new AoeDamageEffect(0, 0, 0, false, false, this, FunctionThreeMaker.targetsAllCreatures(),
						FunctionThreeMaker.determineCreatureDamage(1));
			} else {
				turnsToTremor--;
			}
		}
		return EmptyEffect.create();
	}

}
