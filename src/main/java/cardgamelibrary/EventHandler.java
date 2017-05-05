package cardgamelibrary;

import java.io.Serializable;
import java.util.List;

public interface EventHandler extends Serializable{

	public boolean handles(Event event);
	
	public List<? extends Effect> handle(Event event);
	
}
