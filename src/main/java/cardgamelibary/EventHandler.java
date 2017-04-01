package cardgamelibary;

import java.util.List;

public interface EventHandler {

	public boolean handles(Event event);
	
	public List<? extends Effect> handle(Event event);
	
}
