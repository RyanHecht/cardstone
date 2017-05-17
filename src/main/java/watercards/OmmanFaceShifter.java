package watercards;

import cardgamelibrary.CardType;
import cardgamelibrary.Creature;
import cardgamelibrary.Effect;
import cardgamelibrary.ManaPool;
import cardgamelibrary.Zone;
import effects.EmptyEffect;
import game.Player;
import templates.ActivatableCard;

public class OmmanFaceShifter extends Creature implements ActivatableCard{

	private static final int			defaultMaxHealth	= 6;
	private static final int			defaultAttack		= 3;
	private static final String		defaultImage			= "images/OmmanFaceShifter.jpg";
	private static final String		defaultName			= "Omman Face Shifter";
	private static final String		defaultText			= "Activate for free: swap attack and health.";
	
	public OmmanFaceShifter(Player owner) {
		super(defaultMaxHealth, defaultAttack, new ManaPool(35, 0, 1, 0, 0, 0), defaultImage, owner, defaultName, defaultText, CardType.CREATURE);
	}

	@Override
	public Effect onThisActivated() {
		int tempHealth = this.getHealth();
		this.setHealth(this.getAttack());
		this.setAttack(this.getHealth());
		return EmptyEffect.create();
	}

	@Override
	public boolean canBeActivated(Zone z) {
		return z.equals(Zone.CREATURE_BOARD);
	}
	
	
}
