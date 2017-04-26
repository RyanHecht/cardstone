package cards;

import cardgamelibrary.CardType;
import cardgamelibrary.Creature;
import cardgamelibrary.ManaPool;
import game.Player;

public class CheapJoshCreature extends Creature {

	private static final ManaPool	defaultCost		= new ManaPool(10, 0, 0, 1, 0, 0);
	private static final String		defaultImage	= "images/creature.jpg";
	private static final String		defaultName		= "Cheap Josh Creature";
	private static final String		defaultText		= "BS TESTING THINGY";
	private static final int			defaultHealth	= 5;
	private static final int			defaultAttack	= 4;
	private static final CardType	defaultType		= CardType.CREATURE;

	public CheapJoshCreature(Player owner) {
		super(defaultHealth, defaultAttack, defaultCost, defaultImage, owner, defaultName, defaultText, defaultType);
		// TODO Auto-generated constructor stub
	}

}
