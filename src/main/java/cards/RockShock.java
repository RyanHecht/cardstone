package cards;

import cardgamelibrary.Board;
import cardgamelibrary.Card;
import cardgamelibrary.CardType;
import cardgamelibrary.CreatureInterface;
import cardgamelibrary.Effect;
import cardgamelibrary.ManaPool;
import cardgamelibrary.SpellCard;
import cardgamelibrary.Zone;
import game.Player;
import templates.TargetsOtherCard;
import templates.decorators.CantAttackForTurnsCreature;

public class RockShock extends SpellCard implements TargetsOtherCard {

	public RockShock(Player owner) {
		super(defaultCost, defaultImage, owner, defaultName, defaultText, defaultType);
	}

	private static final ManaPool defaultCost = new ManaPool(15, 0, 0, 1, 0, 0);
	private static final String defaultImage = "images/RockShock.jpg";
	private static final String defaultName = "Rock Shock";
	private static final String defaultText = "Deal two damage to target minion and prevent it from attacking next turn.";
	private static final CardType defaultType = CardType.SPELL;

	public boolean cardValidTarget(Card card, Zone z) {
		if (card.getType() == CardType.CREATURE && z == Zone.CREATURE_BOARD) {
			System.out.println("was valid");
			return true;
		}
		return false;
	}

	// when the target is valid, produce some effect.
	// this should usually also play the targetting card (move to appropriate zone
	// i.e. graveyard).
	public Effect impactCardTarget(Card target, Zone zone) {
		return (Board board) -> {
			System.out.println("tryna");
			if ((target.isA(CreatureInterface.class))) {
				System.out.println("SUCCESS");
				CreatureInterface c = (CreatureInterface) target;
				board.damageCard(c, this, 2);
				board.applyToCard(target, new CantAttackForTurnsCreature(c, 2));
			}
		};
	}

}
