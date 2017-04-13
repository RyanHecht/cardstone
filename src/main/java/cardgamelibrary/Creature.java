package cardgamelibrary;

public interface Creature extends Card {

	public int getHealth();

	public int getAttack();

	/**
	 * gets whether the creature is dead. By default just returns if health < 0,
	 * but of course can be overridden.
	 *
	 * @return
	 */
	default public boolean isDead() {
		return getHealth() <= 0;
	}

}
