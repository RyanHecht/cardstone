package devotions;

import cardgamelibrary.Card;
import cardgamelibrary.DevotionType;
import cardgamelibrary.Event;
import cardgamelibrary.EventType;
import cardgamelibrary.Zone;
import events.CardHealedEvent;
import events.CardZoneChangeEvent;
import events.CreatureAttackEvent;
import game.Player;


//Chaos goes up on playing odd costed cards and choose effects.
//Order goes up on playing even costed cards and choose effects.
public class BalanceDevotion implements Devotion{


	private Player owner;
	private int chaos;
	private int order;

	public BalanceDevotion(Player owner){
		this.owner = owner;
		this.chaos = 0;
		this.order = 0;
	}
	
	public int getChaos(){
		return chaos;
	}
	
	public int getOrder(){
		return order;
	}
	
	public void increaseOrder(int amt){
		order += amt;
	}
	
	public void increaseChaos(int amt){
		chaos+=amt;
	}
	
	@Override
	public DevotionType getDevotionType() {
		return DevotionType.BALANCE;
	}

	@Override
	public Player getOwner() {
		return owner;
	}
	
	public void onCardPlayed(Card c){
		if(c.getOwner().equals(getOwner())){
			if(c.getCost().getResources() % 2 == 0){
				order++;
			}
			else{
				chaos++;
			}
		}
	}

	@Override
	public String getLevel() {
		return "Order: "+order + "<br>Chaos: " + chaos;
	}

}
