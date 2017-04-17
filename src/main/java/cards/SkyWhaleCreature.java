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
public class SkyWhaleCreature extends Creature {
	private static int			maxHealth	= 30;
	private static int			attack		= 5;
	private static ManaPool	cost			= new ManaPool(30, 0, 2, 0, 1, 0);
	private static String		image			= "boardstuff/images/magicSkyWhale.jpg";
	private static String		name			= "Sky Whale";
	private static String		text			= "I'm flying!";

	public SkyWhaleCreature(Player owner) {
		super(maxHealth, attack, cost, image, owner, name, text, CardType.CREATURE);
	}
}
