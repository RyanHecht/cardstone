package cardgamelibrary;

import java.io.Serializable;

/**
 * A type of event which can occur to a card currently on the board
 * @author 42jpa
 *
 */
public enum BoardCardEventType implements Serializable{

	ATTACKED,DEFENDED,TARGETED,DAMAGED,
	
}
