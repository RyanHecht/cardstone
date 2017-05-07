package cards;

import cardgamelibrary.CardType;
import cardgamelibrary.ConcatEffect;
import cardgamelibrary.Creature;
import cardgamelibrary.Effect;
import cardgamelibrary.ManaPool;
import cardgamelibrary.Zone;
import effects.EmptyEffect;
import game.Player;

public class DwarvenMiner extends Creature{

	private static final ManaPool	defaultCost		= new ManaPool(40, 0, 0, 2, 0, 0);
	private static final String		defaultImage	= "images/DwarvenMiner.jpg";
	private static final String		defaultName		= "Dwarven Miner";
	private static final String		defaultText		= "Double the effect of all your auras.";
	private static final int			defaultHealth	= 4;
	private static final int			defaultAttack	= 3;
	private static final CardType	defaultType		= CardType.CREATURE;

	public DwarvenMiner(Player owner) {
		super(defaultHealth, defaultAttack, defaultCost, defaultImage, owner, defaultName, defaultText, defaultType);
	}
	
	public boolean onProposedEffect(Effect e, Zone z) {
		if(z == Zone.CREATURE_BOARD && e.getSrc().getType() 
				== CardType.AURA && e.getSrc().getOwner() == getOwner()){
			return true;
		}
		return false;
	}

	public Effect getNewProposition(Effect e, Zone z) {
		ConcatEffect ce = new ConcatEffect(this);
		ce.addEffect(e);
		ce.addEffect(e);
		return ce;
	}
	
}
