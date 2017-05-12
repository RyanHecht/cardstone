package cards;

import cardgamelibrary.Card;
import cardgamelibrary.CardType;
import cardgamelibrary.CreatureInterface;
import cardgamelibrary.Effect;
import cardgamelibrary.ManaPool;
import cardgamelibrary.SpellCard;
import cardgamelibrary.Zone;
import effects.CardDamageEffect;
import effects.PlayerDamageEffect;
import game.Player;
import templates.TargetsOtherCard;
import templates.TargetsPlayer;

public class JunsBolt extends SpellCard implements TargetsOtherCard, TargetsPlayer {

	private static final String defaultImage = "images/JunsBolt.jpg";
	private static final String defaultName = "Jun's Bolt";
	private static final String defaultText = "Deal 3 damage to target enemy creature or to your opponent.";
	private static final CardType defaultType = CardType.SPELL;

	public JunsBolt(Player owner) {
		super(new ManaPool(15, 0, 0, 0, 1, 0), defaultImage, owner, defaultName, defaultText, defaultType);
	}

	@Override
	public boolean playerValidTarget(Player p) {
		return true;
	}

	@Override
	public boolean cardValidTarget(Card card, Zone targetIn) {
		if (card.isA(CreatureInterface.class) && targetIn == Zone.CREATURE_BOARD) {
			// card must be a creature and on board.
			return true;
		}
		return false;
	}

	@Override
	public Effect impactCardTarget(Card target, Zone targetIn) {
		return new CardDamageEffect(this, (CreatureInterface) target, 3);
	}
	@Override
	public Effect impactPlayerTarget(Player p) {
		return new PlayerDamageEffect(p, this, 3);
	}

}
