package devotions;

import cardgamelibrary.DevotionType;
import game.Player;

public class FireDevotion implements Devotion{

	private Player owner;
	private int level;

	public FireDevotion(Player owner){
		this.owner = owner;
		this.level = 10;
	}
	
	public String getLevel(){
		return "Heat: " + level;
	}
	
	public void useFire(){
		if(level > 0){
			level--;
		}
	}
	
	@Override
	public DevotionType getDevotionType() {
		return DevotionType.FIRE;
	}

	@Override
	public Player getOwner() {
		return owner;
	}

}
