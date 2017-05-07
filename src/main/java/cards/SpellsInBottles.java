package cards;

import cardgamelibrary.Card;
import cardgamelibrary.CardType;
import cardgamelibrary.ConcatEffect;
import cardgamelibrary.Effect;
import cardgamelibrary.ElementType;
import cardgamelibrary.ManaPool;
import cardgamelibrary.SpellCard;
import cardgamelibrary.Zone;
import effects.AddToOccEffect;
import effects.CancelledEffect;
import effects.EmptyEffect;
import effects.SummonEffect;
import game.Player;

public class SpellsInBottles extends SpellCard{
	
	private static final ManaPool	defaultCost		= new ManaPool(20, 0, 0, 0, 1, 0);
	private static final String		defaultImage	= "images/SpellsInBottles.jpg";
	private static final String		defaultName		= "Storms In Bottles";
	private static final String		defaultText		= "Whenever you would play a spell with a cost this turn, instead cancel that spell and remove the spells cost.";
	private static final CardType	defaultType		= CardType.SPELL;
	private boolean active;
	
	public SpellsInBottles(Player owner) {
		super(defaultCost, defaultImage, owner, defaultName, defaultText, defaultType);
		active = false;
	}
	
	public Effect onThisPlayed(Card c, Zone z){
		active = true;
		return EmptyEffect.create();
	}
	
	public Effect onTurnStart(Player p, Zone z){
		active = false;
		return EmptyEffect.create();
	}
	
	public boolean onProposedEffect(Effect e, Zone z){
		if(e.getSrc().getCost().getResources() <= 0){
			return false;
		}
		for(ElementType type : ElementType.values()){
			if(e.getSrc().getCost().getElement(type) <= 0){
				return false;
			}
		}
		if(e.getSrc().getType() == CardType.SPELL){
			return true;
		}
		return false;
	}
	
	public Effect getNewProposition(Effect e, Zone z){
		Card c = e.getSrc();
		getOwner().payCost(c.getCost());
		c.getCost().setResources(0);
		for(ElementType type : ElementType.values()){
			c.getCost().setElement(type, 0);
		}
		return new CancelledEffect(this);
		
	}
	
}
