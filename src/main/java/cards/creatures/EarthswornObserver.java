package cards.creatures;

import cardgamelibrary.Board;
import cardgamelibrary.Card;
import cardgamelibrary.CardType;
import cardgamelibrary.Creature;
import cardgamelibrary.Effect;
import cardgamelibrary.ElementType;
import cardgamelibrary.ManaPool;
import cardgamelibrary.Zone;
import cards.templates.CantAttackCreature;
import game.Player;

public class EarthswornObserver extends Creature implements CantAttackCreature {

	private static final ManaPool	defaultCost		= new ManaPool(25, 0, 0, 1, 0, 0);
	private static final String		defaultImage	= "images/EarthswornObserver.png";
	private static final String		defaultName		= "Earthsworn Observer";
	private static final String		defaultText		= "Can't attack. Whenever a minion with Earth in its cost dies, gain 2 health.";
	private static final int			defaultHealth	= 8;
	private static final int			defaultAttack	= 0;
	private static final CardType	defaultType		= CardType.CREATURE;

	public EarthswornObserver(Player owner) {
		super(defaultAttack, defaultHealth, defaultCost, defaultImage, owner, defaultName, defaultText, defaultType);
	}

	@Override
	public Effect onCreatureDeath(Creature cr, Zone z) {
		if (z == Zone.CREATURE_BOARD) {
			if (cr.getCost().getElement(ElementType.EARTH) > 0) {
				return (Board board) -> {
					board.changeCreatureHealth(this, 2, z);
				};
			}
		}
		return super.onCreatureDeath(cr, z);
	}

	@Override
	public Effect onThisPlayed(Card c, Zone z) {
		return (Board board) -> {
			// play the creature onto the board!
			board.addCardToOcc(this, board.getOcc(getOwner(), Zone.CREATURE_BOARD),
					board.getOcc(getOwner(), Zone.CREATURE_BOARD));
		};
	}

}
