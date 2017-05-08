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
import effects.EffectType;
import effects.EmptyEffect;
import effects.SummonEffect;
import game.Player;

public class SpellsInBottles extends SpellCard{
	private static final String		defaultImage	= "images/SpellsInBottles.jpg";
	private static final String		defaultName		= "Spells In Bottles";
	private static final String		defaultText		= "Whenever you would play a spell with a cost this turn, instead cancel that spell and remove the spells cost.";
	private static final CardType	defaultType		= CardType.SPELL;
	private boolean active;
	
	public SpellsInBottles(Player owner) {
		super(new ManaPool(35, 0, 0, 0, 1, 0), defaultImage, owner, defaultName, defaultText, defaultType);
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
		if(active){
			if(e.getType() != EffectType.CARD_PLAYED){
				System.out.println("it wasn't" + e.getType().name());
				return false;
			}
			else{
				System.out.println("it was the tang");
			}
			if(e.getSrc().getCost().getResources() <= 0){
				int totalCost = 0;
				for(ElementType type : ElementType.values()){
					totalCost += e.getSrc().getCost().getElement(type);
				}
				if(totalCost <= 0){
					return false;
				}
			}
			
			if(e.getSrc().equals(this)){
				return false;
			}
			if(e.getSrc().getType() == CardType.SPELL){
				System.out.println("cancelling it for the new one");
				return true;
			}
		}
		return false;
	}
	
	public Effect getNewProposition(Effect e, Zone z){
		Card c = e.getSrc();
		//getOwner().payCost(c.getCost());
		c.getCost().setResources(0);
		for(ElementType type : ElementType.values()){
			c.getCost().setElement(type, 0);
		}
		return new CancelledEffect(this);
	}
	
}
