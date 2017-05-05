package cards;

import cardgamelibrary.CardType;
import cardgamelibrary.Creature;
import cardgamelibrary.ManaPool;
import game.Player;

/**
 * Magic Sky Whale thingy!
 * 
 * @author Raghu
 *
 */
public class SkyWhale extends Creature {
	private static final int			defaultMaxHealth	= 6;
	private static final int			defaultAttack		= 4;
	private static final ManaPool	defaultCost			= new ManaPool(30, 0, 2, 0, 1, 0);
	private static final String		defaultImage			= "images/magicSkyWhale.jpg";
	private static final String		defaultName			= "Sky Whale";
	private static final String		defaultText			= "Im flying!";

	public SkyWhale(Player owner) {
		super(defaultMaxHealth, defaultAttack, defaultCost, defaultImage, owner, defaultName, defaultText, CardType.CREATURE);
	}

}
