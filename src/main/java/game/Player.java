package game;

import com.google.gson.JsonObject;

import cardgamelibrary.ElementType;
import cardgamelibrary.ManaPool;

/**
 * Class to represent a player in the game.
 *
 * @author Raghu
 *
 */
public class Player {
	private PlayerType	playerType;
	private int					life;
	private ManaPool		manaPool;

	public Player(int l, PlayerType p) {
		playerType = p;
		life = l;
		manaPool = new ManaPool(0, 0, 0, 0, 0, 0);
	}

	public PlayerType getPlayerType() {
		return playerType;
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

	public void setLife(int newLife) {
		life = newLife;
	}

	public void takeDamage(int damage) {
		life -= damage;
	}

	public void healDamage(int heal) {
		life += heal;
	}

	public boolean validateCost(ManaPool cost) {
		return manaPool.canPay(cost);
	}

	public JsonObject jsonifySelf() {
		JsonObject result = new JsonObject();
		result.addProperty("health", life);
		result.addProperty("resources", manaPool.getResources());
		JsonObject elementObject = new JsonObject();
		elementObject.addProperty("fire", manaPool.getElement(ElementType.FIRE));
		elementObject.addProperty("water", manaPool.getElement(ElementType.WATER));
		elementObject.addProperty("air", manaPool.getElement(ElementType.AIR));
		elementObject.addProperty("earth", manaPool.getElement(ElementType.EARTH));
		elementObject.addProperty("balance", manaPool.getElement(ElementType.BALANCE));
		result.add("element", elementObject);
		return result;
	}
}
