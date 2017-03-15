package cardgamelibary;

public interface Creature extends Card{

	/**
	 * gets whether the creature is dead. By default just returns if health < 0, but of course can be overridden.
	 * @return
	 */
	public boolean getIsDead();
	
}
