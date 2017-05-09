package devotions;

import cardgamelibrary.DevotionType;
import cardgamelibrary.Effect;
import game.Player;

public class WaterDevotion implements Devotion{

	
	
	private TideLevel tideLevel;
	private Player owner;

	public WaterDevotion(Player owner) {
		this.owner = owner;
		this.tideLevel = TideLevel.RISING;
	}
	
	public void onTurnStart(Player p){
		if(p == getOwner()){
			tideLevel = tideLevel.next();
		}
	}
	
	public void advanceLevel(){
		tideLevel = tideLevel.next();
	}
	
	public void setTideLevel(TideLevel level){
		tideLevel = level;
	}
	
	public TideLevel getTideLevel(){
		return tideLevel;
	}

	@Override
	public DevotionType getDevotionType() {
		return DevotionType.WATER;
	}

	@Override
	public Player getOwner() {
		return owner;
	}
	

	
	
}
