package devotions;

import com.google.gson.JsonObject;

import cardgamelibrary.CardType;
import cardgamelibrary.DevotionType;
import cardgamelibrary.ElementType;
import cardgamelibrary.ManaPool;
import cardgamelibrary.PlayableCard;
import game.Player;

public class NoDevotion implements Devotion{

	private ElementType devotion;
	private boolean devotionSet;
	private Player player;
	
	public NoDevotion(Player p) {
		this.devotion = null;
		this.devotionSet = false;
		player = p;
	}


	@Override
	public DevotionType getDevotionType() {
		return DevotionType.NO_DEVOTION;
	}


	@Override
	public Player getOwner() {
		return player;
	}

	

}
