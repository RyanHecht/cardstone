package devotions;

import com.google.gson.JsonObject;

import cardgamelibrary.Card;
import cardgamelibrary.CardType;
import cardgamelibrary.DevotionType;
import cardgamelibrary.ElementType;
import cardgamelibrary.Event;
import cardgamelibrary.Jsonifiable;
import cardgamelibrary.ManaPool;
import game.Player;

//TODO needsta logging 
public interface Devotion extends Jsonifiable{

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
	
	public String getLevel();
	
	default public JsonObject jsonifySelf(){
		JsonObject result = new JsonObject();
		result.addProperty("type", getDevotionType().name());
		result.addProperty("level", getLevel());
		return result;
	}
	
	default public JsonObject jsonifySelfChanged() {
		return jsonifySelf();
	}
	
}
