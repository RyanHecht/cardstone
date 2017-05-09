package devotions;

import cardgamelibrary.DevotionType;
import game.Player;

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

}
