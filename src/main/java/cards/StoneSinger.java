package cards;

import cardgamelibrary.CardType;
import cardgamelibrary.Creature;
import cardgamelibrary.ManaPool;
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
	
	
	
	
	private static class StoneGolem extends Creature{
		private static final ManaPool	defaultCost		= new ManaPool(10, 0, 0, 1, 0, 0);
		private static final String		defaultImage	= "images/StoneSinger.jpg";
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
