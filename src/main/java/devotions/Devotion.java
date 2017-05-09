package devotions;

import com.google.gson.JsonObject;

import cardgamelibrary.Card;
import cardgamelibrary.CardType;
import cardgamelibrary.DevotionType;
import cardgamelibrary.ElementType;
import cardgamelibrary.Event;
import cardgamelibrary.ManaPool;
import game.Player;

//TODO needsta logging 
public interface Devotion{

	public DevotionType getDevotionType();
	
	public default void onTurnStart(Player p){
		
	}
	
	public Player getOwner();
	
	public default void onCardPlayed(Card c){
		
	}
	
	public default void onUserAction(Player p){
		
	}

	public default void eventOccurred(Event event){
		
	}
	
}
