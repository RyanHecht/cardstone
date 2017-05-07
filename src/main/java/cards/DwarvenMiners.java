package cards;

import cardgamelibrary.CardType;
import cardgamelibrary.Creature;
import cardgamelibrary.Effect;
import cardgamelibrary.ManaPool;
import cardgamelibrary.Zone;
import effects.EmptyEffect;
import game.Player;

public class DwarvenMiners extends Creature{

	private static final ManaPool	defaultCost		= new ManaPool(30, 0, 0, 2, 0, 0);
	private static final String		defaultImage	= "images/DwarvenMiners.jpg";
	private static final String		defaultName		= "Dwarven Miners";
	private static final String		defaultText		= "Double the effect of all your auras.";
	private static final int			defaultHealth	= 3;
	private static final int			defaultAttack	= 5;
	private static final CardType	defaultType		= CardType.CREATURE;

	public DwarvenMiners(Player owner) {
		super(defaultHealth, defaultAttack, defaultCost, defaultImage, owner, defaultName, defaultText, defaultType);
	}
	
	public boolean onProposedEffect(Effect e, Zone z) {
		if()
		return false;
	}

	public Effect getNewProposition(Effect e, Zone z) {
		return e;
	}
	
}
