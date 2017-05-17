package watercards;

import cardgamelibrary.CardType;
import cardgamelibrary.Creature;
import cardgamelibrary.ManaPool;
import game.Player;

public class Leviathin extends Creature{

	private static final int			defaultMaxHealth	= 5;
	private static final int			defaultAttack		= 5;
	private static final String		defaultImage			= "images/Leviathin.jpg";
	private static final String		defaultName			= "Leviathin";
	private static final String		defaultText			= "What did you expect?";
	
	public Leviathin(Player owner) {
		super(defaultMaxHealth, defaultAttack, new ManaPool(50, 0, 2, 0, 0, 0), defaultImage, owner, defaultName, defaultText, CardType.CREATURE);
	}
	
	
	
}
