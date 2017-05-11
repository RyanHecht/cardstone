package devotions;

import cardgamelibrary.Card;
import cardgamelibrary.CreatureInterface;
import cardgamelibrary.DevotionType;
import game.Player;

public class EarthDevotion implements Devotion{

	private Player owner;
	private boolean willGain;
	private int level;

	public EarthDevotion(Player p){
		this.owner = p;
		this.willGain = false;
	}
	
	public int getSleepingStoneCount(){
		return level;
	}
	
	public void onTurnStart(Player p){
		if(p.equals(getOwner())){
			willGain = true;
		}
		else{
			if(willGain){
				level++;
			}
		}
	}
	
	public void onUserAction(Player p){
		if(p.equals(getOwner())){
			willGain = false;
		}
	}
	
	public String getLevel(){
		return "Sleeping Stone: " + level;
	}
	
	@Override
	public DevotionType getDevotionType() {
		return DevotionType.EARTH;
	}

	@Override
	public Player getOwner() {
		return owner;
	}
	
	public static int getLevelOfEarth(Devotion d){
		if(d.getDevotionType().equals(DevotionType.EARTH)){
			EarthDevotion ed = (EarthDevotion) d;
			return ed.getSleepingStoneCount();
		}
		return 0;
	}

}
