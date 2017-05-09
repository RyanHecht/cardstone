package devotions;

import cardgamelibrary.Card;
import cardgamelibrary.DevotionType;
import cardgamelibrary.ElementType;
import game.Player;

public class AirDevotion implements Devotion{

	private Player owner;
	private int level;

	public AirDevotion(Player owner){
		this.owner = owner;
		this.level = 0;
	}
	
	public void onTurnStart(Player p){
		if(p != owner){
			if(level < 0){
				level = 0;
			}
			else{
				level = level / 3;
			}
		}
	}
	
	@Override
	public DevotionType getDevotionType() {
		return DevotionType.AIR;
	}

	@Override
	public Player getOwner() {
		return owner;
	}
	
	public int getLevel(){
		return level;
	}
	
	public void setLevel(int level){
		this.level = level;
	}
	
	public void increaseLevel(int amt){
		level += amt;
	}

	public void onCardPlayed(Card c){
		if(c.hasElement(ElementType.AIR)){
			if(c.getOwner().equals(getOwner())){
				level++;
			}
		}
	}
}
