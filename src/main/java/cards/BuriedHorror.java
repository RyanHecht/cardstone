package cards;

import cardgamelibrary.Card;
import cardgamelibrary.CardType;
import cardgamelibrary.Creature;
import cardgamelibrary.Effect;
import cardgamelibrary.ManaPool;
import cardgamelibrary.Zone;
import game.Player;

public class BuriedHorror extends Creature{

	private static final ManaPool	defaultCost		= new ManaPool(70, 0, 0, 2, 0, 0);
	private static final String		defaultImage	= "images/creature.jpg";
	private static final String		defaultName		= "Buried Horror";
	private static final String		defaultText		= "Can't attack. Next time an enemy minion attacks, destroy it. Then, this can attack.";
	private static final int			defaultHealth	= 5;
	private static final int			defaultAttack	= 9;
	private static final CardType	defaultType		= CardType.CREATURE;

	public BuriedHorror(Player owner) {
		super(defaultHealth, defaultAttack, defaultCost, defaultImage, owner, defaultName, defaultText, defaultType);
	}
	
	public Effect onThisPlayed(Card c, Zone z){
		
		return null;
	}
	
}
