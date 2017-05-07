package cards;

import cardgamelibrary.Card;
import cardgamelibrary.CardType;
import cardgamelibrary.ConcatEffect;
import cardgamelibrary.Effect;
import cardgamelibrary.ElementType;
import cardgamelibrary.ManaPool;
import cardgamelibrary.SpellCard;
import cardgamelibrary.Zone;
import effects.SummonEffect;
import game.Player;

public class BoonFromTheStorm extends SpellCard{

	private static final ManaPool	defaultCost		= new ManaPool(20, 0, 0, 0, 1, 0);
	private static final String		defaultImage	= "images/BoonFromTheStorm.jpg";
	private static final String		defaultName		= "Boon From The Storm";
	private static final String		defaultText		= "Add 3 'Jun's Bolt' to your hand. You do not have to pay the element cost for these cards.";
	private static final CardType	defaultType		= CardType.SPELL;
	
	public BoonFromTheStorm(Player owner) {
		super(defaultCost, defaultImage, owner, defaultName, defaultText, defaultType);
	}
	
	public Effect onThisPlayed(Card c, Zone z){
		ConcatEffect ce = new ConcatEffect(this);
		for(int x = 0; x < 3; x++){
			JunsBolt jb = new JunsBolt(getOwner());
			jb.getCost().setElement(ElementType.AIR, 0);
			ce.addEffect(new SummonEffect(jb,Zone.HAND,this));
		}
		return ce;
	}
	
}
