package cardgamelibrary;

import com.google.gson.JsonObject;

import game.Player;

public class Creature extends PlayableCard {

	private int	maxHealth;
	private int	health;
	private int	attack;
	private int	attacks;

	public Creature(int maxHealth, int attack, ManaPool cost, String image, Player owner, String name, String text,
			CardType type) {
		super(cost, image, owner, name, text, type);
		// initialize starting health at maxHealth.
		this.maxHealth = maxHealth;
		this.health = maxHealth;
		this.attack = attack;
		this.attacks = 0;
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

	public int getNumAttacks() {
		return attacks;
	}

	public void takeDamage(int damage, Card src) {
		setHealth(getHealth() - damage);
	}

	public void heal(int heal, Card src) {
		setHealth(getHealth() + heal);
		if (health > maxHealth) {
			// can't heal over max health
			health = maxHealth;
		}
	}

	public void setHealth(int newHealth) {
		maxHealth = newHealth;
		health = newHealth;
	}

	/**
	 * gets whether the creature is dead. By default just returns if health < 0,
	 * but of course can be overridden.
	 *
	 * @return a boolean representing whether the creature is dead or not.
	 */
	public boolean isDead() {
		return getHealth() <= 0;
	}

	@Override
	public JsonObject jsonifySelf() {
		JsonObject result = super.jsonifySelf();
		result.addProperty("attack", getAttack());
		result.addProperty("health", getHealth());
		result.addProperty("damaged", getMaxHealth() > getHealth());
		return result;
	}

	@Override
	public Effect onTurnStart(Player p, Zone z) {
		if (p == this.getOwner()) {
			this.attacks = 1;
		}
		return super.onTurnStart(p, z);
	}

	public void changeMaxHealthBy(int amount) {
		this.maxHealth += amount;
		this.health += amount;
	}

}
