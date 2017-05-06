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
	private static final String		defaultText		= "Taunt. On play, gain life equal to the life you have gained this game";
	private static final int			defaultHealth	= 0;
	private static final int			defaultAttack	= 1;
	private static final CardType	defaultType		= CardType.CREATURE;
	private int healthGained;

	public RockInTheWay(Player owner) {
		super(defaultAttack, defaultHealth, defaultCost, defaultImage, owner, defaultName, defaultText, defaultType);
		healthGained = 0;
	}
	
	

	@Override
	public Effect onPlayerHeal(Player p, Card src, int heal, Zone z) {
		return (Board board) -> {
			if (this.getOwner() == p) {
				healthGained += heal;
			}
		};
	}
	
	public Effect onThisPlayed(Card c, Zone z) {
		return (Board board) ->{
			board.changeCreatureHealth(this, healthGained, z);
		};
	}

}
