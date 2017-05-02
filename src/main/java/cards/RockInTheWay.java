package cards;

import cardgamelibrary.Board;
import cardgamelibrary.Card;
import cardgamelibrary.CardType;
import cardgamelibrary.Creature;
import cardgamelibrary.Effect;
import cardgamelibrary.ManaPool;
import cardgamelibrary.Zone;
import effects.EmptyEffect;
import game.Player;
import templates.CantAttackCreature;

public class RockInTheWay extends Creature implements CantAttackCreature {

	private static final ManaPool	defaultCost		= new ManaPool(30, 0, 0, 2, 0, 0);
	private static final String		defaultImage	= "images/RockInTheWay.jpg";
	private static final String		defaultName		= "Rock In The Way";
	private static final String		defaultText		= "Taunt. Can't attack. Attack and life equal to life you have gained this game.";
	private static final int			defaultHealth	= 1;
	private static final int			defaultAttack	= 1;
	private static final CardType	defaultType		= CardType.CREATURE;

	public RockInTheWay(Player owner) {
		super(defaultAttack, defaultHealth, defaultCost, defaultImage, owner, defaultName, defaultText, defaultType);
	}

	@Override
	public Effect onPlayerHeal(Player p, Card src, int heal, Zone z) {
		if (this.getOwner() == p) {
			return (Board board) -> {
				board.changeCreatureHealth(this, heal, z);
				board.changeCreatureAttack(this, heal, z);
			};
		}
		return EmptyEffect.create();
	}

}
