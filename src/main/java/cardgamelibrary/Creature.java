package cardgamelibrary;

import com.google.gson.JsonObject;

public class Creature extends PlayableCard {

	private int	maxHealth;
	private int	health;
	private int	attack;

	public Creature(int health, int attack) {
		maxHealth = health;
		this.health = health;
		this.attack = attack;
	}

	public int getHealth() {
		return health;
	}

	public int getMaxHealth() {
		return maxHealth;
	}

	public int getAttack() {
		return attack;
	}

	public void takeDamage(int damage, Card src) {
		setHealth(getHealth() - damage);
	}

	public void heal(int heal, Card src) {
		setHealth(getHealth() + heal);
	}

	public void setHealth(int newHealth) {
		maxHealth = newHealth;
		health = newHealth;
	}

	/**
	 * gets whether the creature is dead. By default just returns if health < 0,
	 * but of course can be overridden.
	 *
	 * @return
	 */
	public boolean isDead() {
		return getHealth() <= 0;
	}
	
	public JsonObject jsonifySelf(){
		JsonObject result = super.jsonifySelf();
		result.addProperty("attack", getAttack());
		result.addProperty("health",getHealth());
		result.addProperty("damaged",getMaxHealth() > getHealth());
		return result;
	}

}
