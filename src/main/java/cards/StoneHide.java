package cards;

import cardgamelibrary.AuraCard;
import cardgamelibrary.Board;
import cardgamelibrary.Card;
import cardgamelibrary.CardType;
import cardgamelibrary.Effect;
import cardgamelibrary.ElementType;
import cardgamelibrary.Event;
import cardgamelibrary.EventType;
import cardgamelibrary.ManaPool;
import cardgamelibrary.Zone;
import events.PlayerDamagedEvent;
import game.Player;

public class StoneHide extends AuraCard{

	private static final ManaPool defaultCost = new ManaPool(20, 0, 0, 2, 0, 0);
	private static final String defaultImage = "images/StoneHide.jpg";
	private static final String defaultName = "Stone Hide";
	private static final String defaultText = "Reduce all instances of damage by 1";
	private static final CardType defaultType = CardType.AURA;

	public StoneHide(Player owner) {
		super(defaultCost, defaultImage, owner, defaultName, defaultText, defaultType);
	}
	
	public boolean onProposedEvent(Event e, Zone z) {
		if(z != Zone.AURA_BOARD){
			return false;
		}
		
		if(e.getType() == EventType.PLAYER_DAMAGED){
			PlayerDamagedEvent pde = (PlayerDamagedEvent) e;
			if(pde.getPlayer().equals(this.getOwner())){
				if(pde.getDmg() > 0){
					return true;
				}
			}
		}
		return false;
	}
	
	public Event getNewProposition(Event e, Zone z) {
		System.out.println("making new proposition");
			PlayerDamagedEvent old = (PlayerDamagedEvent) e;
			PlayerDamagedEvent newEvent = new PlayerDamagedEvent(old.getSrc(), old.getPlayer(), old.getDmg() - 1);
			return newEvent;
	}
}
