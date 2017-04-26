package cardgamelibrary;

import com.google.gson.JsonObject;

import game.Player;

public class Element extends PlayableCard {

	public static final int DEFAULT_ELEMENT_GAIN = 3;

	public Element(ManaPool cost, String image, Player owner, String name, String text, CardType type) {
		super(cost, image, owner, name, text, type);
		// TODO Auto-generated constructor stub
	}

	@Override
	public JsonObject jsonifySelf() {
		JsonObject result = super.jsonifySelf();
		result.addProperty("elementType", this.getName());
		return result;
	}

}
