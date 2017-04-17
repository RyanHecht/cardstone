package cards;

import cardgamelibrary.Card;
import cardgamelibrary.CardType;
import cardgamelibrary.Creature;
import cardgamelibrary.Effect;
import cardgamelibrary.ManaPool;
import effects.EmptyEffect;
import effects.PlayerDamageEffect;
import game.Player;

/**
 * Stub creature class that will be used to test! Implements default isDead
 * method.
 *
 * @author Raghu
 *
 */
public class StubCreature extends Creature {
	private static int			maxHealth	= 20;
	private static int			attack		= 10;
	private static ManaPool	cost			= new ManaPool(10, 1, 1, 1, 1, 1);
	private static String		image			= "boardstuff/images/creature.jpg";
	private static String		name			= "Stub McStubbington";
	private static String		text			= "I'm the coolest creature around.";

	public StubCreature(Player owner) {
		super(maxHealth, attack, cost, image, owner, name, text, CardType.CREATURE);
	}

	@Override
	public Effect onPlayerDamage(Player p, Card src, int dmg) {
		if (p.getPlayerType() != getOwner().getPlayerType()) {
			// if the player that took damage isn't the owner of this card, deal
			// damage to them.
			if (!(src.getName().equals(this.getName()))) {
				// name check b/c don't want this to proc itself or other
				// instances of itself.
				return new PlayerDamageEffect(this, p, 1);
			}
		}
		return EmptyEffect.create();
	}

}
