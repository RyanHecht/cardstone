package cards;

import cardgamelibrary.CardType;
import cardgamelibrary.Creature;
import cardgamelibrary.ManaPool;
import game.Player;

public class BaboonFromTheStorm extends Creature{

	private static final int			defaultMaxHealth	= 1;
	private static final int			defaultAttack		= 0;
	private static final ManaPool	defaultCost			= new ManaPool(0, 0, 0, 0, 1, 0);
	private static final String		defaultImage			= "images/BaboonFromTheStorm.jpg";
	private static final String		defaultName			= "Baboon From The Storm";
	private static final String		defaultText			= "Where'd this guy come from...";
	
	public BaboonFromTheStorm(Player owner) {
		super(defaultMaxHealth, defaultAttack, defaultCost, defaultImage, owner, defaultName, defaultText, CardType.CREATURE);
	}

}
