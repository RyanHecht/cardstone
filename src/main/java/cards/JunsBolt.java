package cards;

import cardgamelibrary.Board;
import cardgamelibrary.Card;
import cardgamelibrary.CardType;
import cardgamelibrary.Creature;
import cardgamelibrary.Effect;
import cardgamelibrary.ManaPool;
import cardgamelibrary.SpellCard;
import cardgamelibrary.Zone;
import game.Player;
import templates.TargetsOtherCard;
import templates.TargetsPlayer;

public class JunsBolt extends SpellCard implements TargetsOtherCard, TargetsPlayer {

	private static final ManaPool	defaultCost		= new ManaPool(10, 0, 0, 0, 1, 0);
	private static final String		defaultImage	= "images/JunsBolt.jpg";
	private static final String		defaultName		= "Jun's Bolt";
	private static final String		defaultText		= "Deal 3 damage to target creature or player.";
	private static final CardType	defaultType		= CardType.SPELL;

	public JunsBolt(Player owner) {
		super(defaultCost, defaultImage, owner, defaultName, defaultText, defaultType);
	}

	@Override
	public boolean playerValidTarget(Player p) {
		if (p.equals(getOwner())) {
			// can't cast on self.
			return false;
		}
		return true;
	}

	@Override
	public boolean cardValidTarget(Card card, Zone targetIn) {
		if (card.getOwner().equals(getOwner())) {
			// can't cast on own creatures.
			return false;
		}
		if (card instanceof Creature && targetIn == Zone.CREATURE_BOARD) {
			// card must be a creature and on board.
			return true;
		}
		return false;
	}

	@Override
	public Effect impactCardTarget(Card target, Zone targetIn) {
		return (Board board) -> {
			assert (cardValidTarget(target, targetIn));
			// deal 3 damage to targeted creature.
			board.damageCard((Creature) target, this, 3);
		};
	}

	@Override
	public Effect impactPlayerTarget(Player p) {
		return (Board board) -> {
			assert (playerValidTarget(p));
			// deal 3 damage to targeted player.
			board.damagePlayer(p, this, 3);

			};
	}

}
