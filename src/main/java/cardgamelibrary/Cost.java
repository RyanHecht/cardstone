package cardgamelibrary;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.JsonObject;

/**
 * Class to represent the cost of playing a card.
 *
 * @author Raghu
 *
 */
public class Cost {
	private Map<ElementType, Integer>	elementMap;
	private int												resources;

	public Cost(int resources, int fire, int water, int earth, int air, int balance) {
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
	 * Gets the specific elemental cost.
	 *
	 * @param elem
	 *          the element we are looking for the cost of.
	 * @return the cost in that element.
	 */
	public int getElement(ElementType elem) {
		return elementMap.get(elem);
	}
	
	public JsonObject jsonifySelf(){
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
