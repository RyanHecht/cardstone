package cards;

import cardgamelibrary.Card;
import cardgamelibrary.CardType;
import cardgamelibrary.Creature;
import cardgamelibrary.Effect;
import cardgamelibrary.Event;
import cardgamelibrary.EventType;
import cardgamelibrary.ManaPool;
import cardgamelibrary.Zone;
import effects.EmptyEffect;
import events.CardTargetedEvent;
import events.CreatureAttackEvent;
import events.PlayerAttackEvent;
import game.Player;
import templates.ActivatableCard;

public class SandWurm extends Creature implements ActivatableCard{

	private static final String defaultImage = "images/SandWurm.jpg";
	  private static final String defaultName = "Sand Wurm";
	  private static final String defaultText = "Activate for 10 resource to bury itself and become untargetable next turn.";
	  private static final int defaultHealth = 7;
	  private static final int defaultAttack = 5;
	  private static final CardType defaultType = CardType.CREATURE;
	  private boolean active;

	  public SandWurm(Player owner) {
	    super(defaultHealth, defaultAttack, new ManaPool(50, 0, 0, 2, 0, 0),
	        defaultImage, owner, defaultName, defaultText, defaultType);
	    active = false;
	  }

	@Override
	public Effect onThisActivated() {
		active = true;
		return EmptyEffect.create();
	}
	
	
	public Effect onturnStart(Player p, Zone z){
		if(p.equals(getOwner())){
			active = false;
		}
		return EmptyEffect.create();
	}
	
	public boolean onProposedLegalityEvent(Event e, Zone z) {
		if(active){
			if (e.getType() == EventType.CREATURE_ATTACKED) {
				CreatureAttackEvent eve = (CreatureAttackEvent) e;
				return eve.getTarget().equals(this);
			} else if (e.getType() == EventType.CARD_TARGETED) {
				CardTargetedEvent eve = (CardTargetedEvent) e;
				return eve.getTargeted().equals(this);
			}
		}
		return false;
	}
	
	public String getComplaint(Event e, Zone z){
		return "That creature is currently unable to be targeted";
	}

	@Override
	public boolean canBeActivated(Zone z) {
		if(!z.equals(Zone.CREATURE_BOARD)){
			return false;
		}
		if(active){
			return false;
		}
		return getOwner().validateCost(new ManaPool(10,0,0,0,0,0));
	}

}
