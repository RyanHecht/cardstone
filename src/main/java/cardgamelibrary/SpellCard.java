package cardgamelibrary;

import game.Player;
import com.google.gson.JsonObject;

public class SpellCard extends PlayableCard {

	public SpellCard(ManaPool cost, String image, Player owner, String name, String text, CardType type) {
		super(cost, image, owner, name, text, type);
	}
	
	public JsonObject JsonifySelf(){
		JsonObject result = super.jsonifySelf();
		result.addProperty("type", "spell");
		return result;
	}

}
