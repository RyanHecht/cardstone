package cardgamelibrary;

import java.io.Serializable;

import com.google.gson.JsonObject;

public interface Jsonifiable extends Serializable{

	
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
