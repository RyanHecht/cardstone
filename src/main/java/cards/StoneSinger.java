package cards;

import cardgamelibrary.Board;
import cardgamelibrary.Card;
import cardgamelibrary.CardType;
import cardgamelibrary.Creature;
import cardgamelibrary.Effect;
import cardgamelibrary.ElementType;
import cardgamelibrary.ManaPool;
import cardgamelibrary.Zone;
import game.Player;
import templates.decorators.TauntCreature;

public class StoneSinger extends Creature{

	private static final ManaPool	defaultCost		= new ManaPool(45, 0, 0, 1, 0, 0);
	private static final String		defaultImage	= "images/StoneSinger.jpg";
	private static final String		defaultName		= "Stone Singer";
	private static final String		defaultText		= "Whenever you play a card with earth in its cost, summon a 2/2 stone golem with taunt.";
	private static final int			defaultHealth	= 5;
	private static final int			defaultAttack	= 4;
	private static final CardType	defaultType		= CardType.CREATURE;

	public StoneSinger(Player owner) {
		super(defaultHealth, defaultAttack, defaultCost, defaultImage, owner, defaultName, defaultText, defaultType);
	}
	
	public Effect onOtherCardPlayed(Card c, Zone z){
		return (Board board) -> {
			if(z != Zone.CREATURE_BOARD){
				if(c.getCost().getElement(ElementType.EARTH) >= 1){
					TauntCreature tc = new TauntCreature(new StoneGolem(this.getOwner()));
					board.summonCard(tc, Zone.CREATURE_BOARD);
				}
			}
		};
	}

	private static class StoneGolem extends Creature{
		private static final ManaPool	defaultCost		= new ManaPool(10, 0, 0, 1, 0, 0);
		private static final String		defaultImage	= "images/StoneGolem.jpg";
		private static final String		defaultName		= "Stone Golem";
		private static final String		defaultText		= "Whenever you play a card with earth in its cost, summon a 2/2 stone golem with taunt.";
		private static final int			defaultHealth	= 2;
		private static final int			defaultAttack	= 2;
		private static final CardType	defaultType		= CardType.CREATURE;

		public StoneGolem(Player owner) {
			super(defaultHealth, defaultAttack, defaultCost, defaultImage, owner, defaultName, defaultText, defaultType);
		}
	}
	
	
}
