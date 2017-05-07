package cards;

import cardgamelibrary.AuraCard;
import cardgamelibrary.Card;
import cardgamelibrary.CardType;
import cardgamelibrary.ConcatEffect;
import cardgamelibrary.CreatureInterface;
import cardgamelibrary.Effect;
import cardgamelibrary.ManaPool;
import cardgamelibrary.Zone;
import effects.AddToOccEffect;
import effects.AoeDamageEffect;
import effects.EmptyEffect;
import game.Player;
import lambda.FunctionThreeMaker;
import templates.OnAnyAttackCard;

public class Undermine extends AuraCard implements OnAnyAttackCard {

	private static final ManaPool defaultCost = new ManaPool(20, 0, 0, 2, 0, 0);
	private static final String defaultImage = "images/Undermine.jpg";
	private static final String defaultName = "Undermine!";
	private static final String defaultText = "If an enemy minion attacks next turn, deal 4 to all minions.";
	private static final CardType defaultType = CardType.SPELL;
	private int turnsLeft;
	private int damage = 4;

	public Undermine(Player owner) {
		super(defaultCost, defaultImage, owner, defaultName, defaultText, defaultType);
		turnsLeft = 0;
	}

	@Override
	public Effect onThisPlayed(Card c, Zone z) {
		assert this == c;
		turnsLeft = 2;
		return EmptyEffect.create();

	}

	public Effect onTurnStart(Player p, Zone z) {
		if (z == Zone.AURA_BOARD) {
			turnsLeft--;
			if (turnsLeft <= 0) {
				return new AddToOccEffect(this, getOwner(), Zone.GRAVE, Zone.AURA_BOARD, this);
			}
		}
		return EmptyEffect.create();
	}

	public Effect onAnyAttack(CreatureInterface attacker, Zone z) {
		if (turnsLeft > 0 && z == Zone.AURA_BOARD) {
			// concat effect to deal damage and then send the card to the grave.
			ConcatEffect ce = new ConcatEffect(this);
			ce.addEffect(new AoeDamageEffect(4, 0, 0, false, false, this, FunctionThreeMaker.targetsAllCreatures(),
					FunctionThreeMaker.determineCreatureDamage(0)));
			ce.addEffect(new AddToOccEffect(this, getOwner(), Zone.GRAVE, Zone.AURA_BOARD, this));
			return ce;
		}
		return EmptyEffect.create();
	}
}
