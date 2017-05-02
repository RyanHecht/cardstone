package cards;

import cardgamelibrary.Card;
import cardgamelibrary.CardType;
import cardgamelibrary.Effect;
import cardgamelibrary.Event;
import cardgamelibrary.EventType;
import cardgamelibrary.ManaPool;
import cardgamelibrary.SpellCard;
import cardgamelibrary.Zone;
import effects.EmptyEffect;
import events.EventCancelledEvent;
import events.PlayerDamagedEvent;
import game.Player;

public class ProtectedBySand extends SpellCard{
	private int spellsToPrevent;

	public ProtectedBySand(Player owner) {
		super(defaultCost, defaultImage, owner, defaultName, defaultText, defaultType);
		spellsToPrevent = 0;
	}
	private static final ManaPool	defaultCost		= new ManaPool(10, 0, 0, 1, 0, 0);
	private static final String		defaultImage	= "images/ProtectedBySand.png";
	private static final String		defaultName		= "Protected By Sand";
	private static final String		defaultText		= "Prevent the next two instances of damage you would take.";
	private static final CardType	defaultType		= CardType.SPELL;
	
	public boolean onProposedEvent(Event e, Zone z){
		if(z == Zone.SPELL_GRAVE){
			if(this.spellsToPrevent > 0){
				if(e.getType() == EventType.PLAYER_DAMAGED){
					PlayerDamagedEvent pde = (PlayerDamagedEvent) e;
					if(pde.getPlayer() == this.getOwner()){
						return true;
					}
				}
			}
		}
		return false;
	}
	
	public Event getNewPropositon(Event e, Zone z){
		if(z == Zone.SPELL_GRAVE){
			if(this.spellsToPrevent > 0){
				if(e.getType() == EventType.PLAYER_DAMAGED){
					PlayerDamagedEvent pde = (PlayerDamagedEvent) e;
					if(pde.getPlayer() == this.getOwner()){
						spellsToPrevent--;
						e = new EventCancelledEvent(e,this);
					}
				}
			}
		}
		return e;
	}
	
	public Effect onThisPlayed(Card c, Zone z){
		assert c == this;
		spellsToPrevent=2;
		return EmptyEffect.create();
	}
	
	
}
