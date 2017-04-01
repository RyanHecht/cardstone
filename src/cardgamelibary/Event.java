package cardgamelibary;

import java.util.List;

public interface Event {

	public EventType getType();
	
	// not sure if this should take a board or not,
	// but it seems necessary unless we are providing
	// some global board access point.
	public List<Card> getAffected(Board board);
	
}
