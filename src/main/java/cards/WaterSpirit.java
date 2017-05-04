package cards;

import cardgamelibrary.CardType;
import cardgamelibrary.Creature;
import cardgamelibrary.ManaPool;
import game.Player;

public class WaterSpirit extends Creature {

	private static final ManaPool	defaultCost		= new ManaPool(10, 0, 3, 0, 0, 0);
	private static final String		defaultImage	= "images/WaterSpirit.jpg";
	private static final String		defaultName		= "Water Spirit";
	private static final String		defaultText		= "";
	private static final int			defaultHealth	= 3;
	private static final int			defaultAttack	= 2;
	private static final CardType	defaultType		= CardType.CREATURE;

	public WaterSpirit(Player owner) {
		super(defaultHealth, defaultAttack, defaultCost, defaultImage, owner, defaultName, defaultText, defaultType);
	}

}
