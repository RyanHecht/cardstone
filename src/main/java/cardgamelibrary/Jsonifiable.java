package cardgamelibrary;

import com.google.gson.JsonObject;

public interface Jsonifiable {

	
	/**
	 * Returns a Json representation of this object.
	 * @return A JsonObject representing this object.
	 */
	public JsonObject jsonifySelf();
	
	
	/**
	 * Returns a Json representation of this object, with different results
	 * depending on whether or not the object (or subobjects) have changed.
	 * @return A JsonObject, representing this object.
	 */
	public JsonObject jsonifySelfChanged();
	
}
