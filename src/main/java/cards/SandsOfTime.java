package cards;

import java.util.LinkedList;
import java.util.List;

import cardgamelibrary.Card;
import cardgamelibrary.CardType;
import cardgamelibrary.ConcatEffect;
import cardgamelibrary.Effect;
import cardgamelibrary.ManaPool;
import cardgamelibrary.SpellCard;
import cardgamelibrary.Zone;
import effects.EmptyEffect;
import effects.SummonEffect;
import game.Player;

public class SandsOfTime extends SpellCard{

	private static final String defaultImage = "images/SandsOfTime.jpg";
	private static final String defaultName = "Sands Of Time";
	private static final String defaultText = "Gain a copy of every aura your played this game. They cost 10 less resource.";
	private static final CardType defaultType = CardType.SPELL;
	private List<Card> aurasPlayed;

	
	public SandsOfTime(Player owner) {
		super(new ManaPool(50, 0, 0, 0, 3, 0), defaultImage, owner, defaultName, defaultText, defaultType);
		aurasPlayed = new LinkedList<Card>();
	}
	
	public Effect onOtherCardPlayed(Card c, Zone z){
		if(c.getType() == CardType.AURA){
			aurasPlayed.add(c);
		}
		return EmptyEffect.create();
	}
	
	public Effect onThisPlayed(Card c, Zone z){
		ConcatEffect ce = new ConcatEffect(this);
		for(Card aura : aurasPlayed){
			Card newAura = aura.getNewInstanceOf(getOwner());
			if(newAura.getCost().getResources() <= 10){
				newAura.getCost().setResources(0);
			}
			else{
				newAura.getCost().setResources(newAura.getCost().getResources() - 10);
			}
			ce.addEffect(new SummonEffect(aura,Zone.HAND,this));
		}
		return ce;
	}
	
}
