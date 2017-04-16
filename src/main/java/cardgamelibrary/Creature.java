package cardgamelibrary;

public interface Creature extends Card {

	public int getHealth();

	public int getMaxHealth();

	public int getAttack();

	default public void takeDamage(int damage, Card src) {
		setHealth(getHealth() - damage);
	}

	default public void heal(int heal, Card src) {
		setHealth(getHealth() + heal);
	}

	public void setHealth(int newHealth);

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
