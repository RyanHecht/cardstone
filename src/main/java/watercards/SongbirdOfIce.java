package watercards;

import cardgamelibrary.CardType;
import cardgamelibrary.Creature;
import cardgamelibrary.ManaPool;
import game.Player;

public class SongbirdOfIce extends Creature{

	private static final int			defaultMaxHealth	= 2;
	private static final int			defaultAttack		= 2;
	private static final String		defaultImage			= "images/SongbirdOfIce.jpg";
	private static final String		defaultName			= "Songbird of Ice";
	private static final String		defaultText			= "Whenever you play a spell, freeze a random enemy minion.";
	
	public OmmosGuard(Player owner) {
		super(defaultMaxHealth, defaultAttack, new ManaPool(50, 0, 2, 0, 0, 0), defaultImage, owner, defaultName, defaultText, CardType.CREATURE);
	}
	
}
