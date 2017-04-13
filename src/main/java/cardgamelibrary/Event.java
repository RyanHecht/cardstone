package cardgamelibrary;

import java.util.List;

public interface Event {

	public EventType getType();
	
	public List<Card> getAffected();
	
}
