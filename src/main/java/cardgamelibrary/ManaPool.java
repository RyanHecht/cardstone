package cardgamelibrary;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.JsonObject;

public class ManaPool {
	private int												resources;
	private Map<ElementType, Integer>	elementMap;
	private static final ManaPool			empty	= new ManaPool(0, 0, 0, 0, 0, 0);

	public ManaPool(int resources, int fire, int water, int earth, int air, int balance) {
		this.resources = resources;
		// fill out map
		elementMap = new HashMap<>();

		elementMap.put(ElementType.FIRE, fire);
		elementMap.put(ElementType.WATER, water);
		elementMap.put(ElementType.EARTH, earth);
		elementMap.put(ElementType.AIR, air);
		elementMap.put(ElementType.BALANCE, balance);
	}

	/**
	 * Gets the resource cost.
	 *
	 * @return the cost in resources.
	 */
	public int getResources() {
		return resources;
	}

	/**
	 * Changes the resources.
	 *
	 * @param newResources
	 *          the new amount of resources.
	 */
	public void setResources(int newResources) {
		resources = newResources;
	}

	/**
	 * Gets the specific elemental cost.
	 *
	 * @param elem
	 *          the element we are looking for the cost of.
	 * @return the cost in that element.
	 */
	public int getElement(ElementType elem) {
		return elementMap.get(elem);
	}

	/**
	 * Changes the value of a specified element.
	 *
	 * @param elem
	 *          the element in question.
	 * @param newAmt
	 *          the new value of the element.
	 */
	public void setElement(ElementType elem, int newAmt) {
		elementMap.put(elem, newAmt);
	}

	/**
	 * checks to see if you have sufficient resources to use expense.
	 *
	 * @param expense
	 *          the manapool we are trying to play.
	 * @return a boolean representing if you can play expense.
	 */
	public boolean canPay(ManaPool expense) {
		for (ElementType et : ElementType.values()) {
			if (this.getElement(et) < expense.getElement(et)) {
				return false;
			}
		}
		return (this.getResources() >= expense.getResources());
	}

	/**
	 * Pays the cost of a ManaPool from this ManaPool.
	 *
	 * @param cost
	 *          the ManaPool we are using this pool to pay.
	 */
	public void payCost(ManaPool cost) {
		// should only call this if we can pay the cost.

		for (ElementType et : ElementType.values()) {
			// for a given element decrement this pool's value by the value in cost.
			this.setElement(et, this.getElement(et) - cost.getElement(et));
		}
		// decrement resources.
		this.setResources(this.getResources() - cost.getResources());
	}

	/**
	 * Static method to generate empty manapools.
	 *
	 * @return the empty ManaPool.
	 */
	public static ManaPool emptyPool() {
		return empty;
	}

	public JsonObject jsonifySelf() {
		JsonObject result = new JsonObject();
		result.addProperty("resources", resources);
		result.addProperty("fire", elementMap.get(ElementType.FIRE));
		result.addProperty("air", elementMap.get(ElementType.AIR));
		result.addProperty("water", elementMap.get(ElementType.WATER));
		result.addProperty("balance", elementMap.get(ElementType.BALANCE));
		result.addProperty("earth", elementMap.get(ElementType.EARTH));
		return result;
	}
}
