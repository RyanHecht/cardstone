package game;

<<<<<<<HEAD=======

import java.util.HashMap;
import java.util.Map;

import com.google.gson.JsonObject;

>>>>>>>594232f c939973c742f35e1dd720dc1ef2fa674f
import cardgamelibrary.ElementType;
import cardgamelibrary.ManaPool;

/**
 * Class to represent a player in the game.
 *
 * @author Raghu
 *
 */
public class Player {
	private int				life;
	private ManaPool	manaPool;

	public Player(int l) {
		life = l;
		manaPool = new ManaPool(0, 0, 0, 0, 0, 0);
	}

	public int getLife() {
		return life;
	}

	public void changeResources(int newCount) {
		manaPool.setResources(newCount);
	}

	public void setElement(ElementType type, int elem) {
		manaPool.setElement(type, elem);
	}

	public int getResources() {
		return manaPool.getResources();
	}

	public int getElem(ElementType type) {
		return manaPool.getElement(type);
	}

	protected void setLife(int newLife) {
		life = newLife;
	}

	protected void takeDamage(int damage) {
		life -= damage;
	}

	protected void healDamage(int heal) {
		life += heal;
	}

	public boolean validateCost(ManaPool cost) {
		return manaPool.canPay(cost);
}	public JsonObject jsonifySelf() {
		JsonObject result = new JsonObject();
		result.addProperty("health", life);
		result.addProperty("pool", resources);
		JsonObject elementObject = new JsonObject();
		elementObject.addProperty("fire", elementMap.get(ElementType.FIRE));
		elementObject.addProperty("water", elementMap.get(ElementType.WATER));
		elementObject.addProperty("air", elementMap.get(ElementType.AIR));
		elementObject.addProperty("earth", elementMap.get(ElementType.EARTH));
		elementObject.addProperty("balance", elementMap.get(ElementType.BALANCE));
		result.add("element", elementObject);
		return result;
	}
}
