package cardgamelibary;

import java.util.List;



/**
 * A card in the game.
 * @author 42jpa
 *
 */
public interface Card {

	
	//Doesn't have to be unique
	String getName();
	
	//Must be unique. We should register all cards somehow and assign ids as we do to prevent conflicting ids
	int getId();
	
	//can return an empty string, doesn't hafta be unique.
	String getText();
	
	String getImage();
	//flyweight getting thing for all the image for the given name by ID
	
	/**
	 * Get the type of the card. Basically will be used instead of putting instanceof all over the place.
	 * @return
	 */
	CardType getType();

	List<EventHandler> getHandlers();
	
}
