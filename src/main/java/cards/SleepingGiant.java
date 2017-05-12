package cards;

import cardgamelibrary.Card;
import cardgamelibrary.CardType;
import cardgamelibrary.Creature;
import cardgamelibrary.ManaPool;
import game.Player;
import templates.CantAttackWhileCreature;

public class SleepingGiant extends Creature implements CantAttackWhileCreature{

	private static final String		defaultImage	= "images/SleepingGiant.jpg";
	private static final String		defaultName		= "Sleeping Giant";
	private static final String		defaultText		= "Can't attack until it takes damage.";
	private static final int			defaultHealth	= 5;
	private static final int			defaultAttack	= 5;
	private static final CardType	defaultType		= CardType.CREATURE;
	private boolean sleeping;


	public SleepingGiant(Player owner) {
		super(defaultHealth, defaultAttack, new ManaPool(20, 0, 0, 1, 0, 0), defaultImage, owner, defaultName, defaultText, defaultType);
		this.sleeping = true;
	}

	public void takeDamage(int dmg, Card src){
		sleeping = false;
	}
	
	@Override
	public boolean attackAllowedYet() {
		return !sleeping;
	}
	
}
