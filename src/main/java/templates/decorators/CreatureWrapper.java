package templates.decorators;

import cardgamelibrary.Card;
import cardgamelibrary.Creature;
import cardgamelibrary.CreatureInterface;

public class CreatureWrapper extends CardWrapper implements CreatureInterface{

	private CreatureInterface creatureInternal;

	public CreatureWrapper(CreatureInterface c) {
		super(c);
		creatureInternal = c;
	}

	public int getHealth() {
		return creatureInternal.getHealth();
	}

	public int getMaxHealth() {
		return creatureInternal.getMaxHealth();
	}

	public int getAttack() {
		return creatureInternal.getAttack();
	}

	public int getNumAttacks() {
		return creatureInternal.getNumAttacks();
	}

	/**
	 * Tells you if a given creature can still attack.
	 *
	 * @return a boolean representing whether or not the creature can still
	 *         attack.
	 */
	public boolean canAttack() {
		return creatureInternal.canAttack();
	}

	public void takeDamage(int damage, Card src) {
		creatureInternal.takeDamage(damage, src);
	}

	public void heal(int heal, Card src) {
		creatureInternal.heal(heal, src);
	}

	public void setHealth(int newHealth) {
		creatureInternal.setHealth(newHealth);
	}

	/**
	 * gets whether the creature is dead. By default just returns if health < 0,
	 * but of course can be overridden.
	 *
	 * @return a boolean representing whether the creature is dead or not.
	 */
	public boolean isDead() {
		return creatureInternal.isDead();
	}

	public void setAttacks(int attacks) {
		creatureInternal.setAttacks(attacks);
	}

	public void changeMaxHealthBy(int amount) {
		creatureInternal.changeMaxHealthBy(amount);
	}

	public void changeAttackBy(int amount) {
		creatureInternal.changeAttackBy(amount);
	}

}
