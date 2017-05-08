package cards;

import cardgamelibrary.CardType;
import cardgamelibrary.Creature;
import cardgamelibrary.ManaPool;
import game.Player;

public class BuriedHorror extends Creature{

	private static final String		defaultImage	= "images/BurriedHorror.jpg";
	private static final String		defaultName		= "Buried Horror";
	private static final String		defaultText		= "Can't attack. Next time an enemy minion attacks, instead destroy it and this can attack.";
	private static final int			defaultHealth	= 9;
	private static final int			defaultAttack	= 6;
	private static final CardType	defaultType		= CardType.CREATURE;


	public BuriedHorror(Player owner) {
		super(defaultHealth, defaultAttack, new ManaPool(70, 0, 0, 2, 0, 0), defaultImage, owner, defaultName, defaultText, defaultType);
	}

	
}
