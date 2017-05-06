package cardgamelibrary;

import com.google.gson.JsonObject;

import effects.AddToOccEffect;
import effects.CardDamageEffect;
import effects.EmptyEffect;
import effects.PlayerDamageEffect;
import game.Player;

public class Creature extends PlayableCard implements CreatureInterface {

	private int maxHealth;
	private int health;
	private int attack;
	private int attacks;

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

	/**
	 * Tells you if a given creature can still attack.
	 *
	 * @return a boolean representing whether or not the creature can still
	 *         attack.
	 */
	public boolean canAttack() {
		return attacks > 0;
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
	public Effect onCardPlayed(Card c, Zone z) {
		// cards that have effects that trigger when THEY are played activate stuff
		// via this.
		ConcatEffect effect = new ConcatEffect();
		if (c.equals(this) && z == Zone.HAND) {
			// pay cost of the card.
			getOwner().payCost(getCost());
			effect.addEffect(new AddToOccEffect(this,getOwner(),Zone.CREATURE_BOARD,Zone.HAND));

			// add any specific effects for this creature being played.
			effect.addEffect(onThisPlayed(c, z));
		}
		else{
			effect.addEffect(onOtherCardPlayed(c,z));
		}
		return effect;
	}

	@Override
	public Effect onCreatureAttack(CreatureInterface attacker, CreatureInterface target, Zone z) {
		if (attacker.equals(this)) {
			// assert we can still attack.
			assert (getNumAttacks() > 0);
			// decrement the number of times it can attack.
			attacker.setAttacks(attacker.getNumAttacks() - 1);
			return new CardDamageEffect(attacker, target, attacker.getAttack());
		}
		if (target.equals(this)) {
			return new CardDamageEffect(target, attacker, target.getAttack());
		}
		return EmptyEffect.create();
	}

	@Override
	public Effect onPlayerAttack(CreatureInterface attacker, Player target, Zone z) {
		if (attacker.equals(this)) {
			// assert we can still attack.
			assert (getNumAttacks() > 0);
			// decrement the number of times it can attack.
			attacker.setAttacks(attacker.getNumAttacks() - 1);
			// damage player by the card's attack.
			return new PlayerDamageEffect(target, attacker, attacker.getAttack());
		}
		return EmptyEffect.create();
	}

	@Override
	public JsonObject jsonifySelf() {
		JsonObject result = super.jsonifySelf();
		result.addProperty("attack", getAttack());
		result.addProperty("health", getHealth());
		result.addProperty("damaged", getMaxHealth() > getHealth());
		result.addProperty("type", "creature");
		return result;
	}

	@Override
	public Effect onTurnStart(Player p, Zone z) {
		System.out.println(getId() + " has received turn start event.");
		if (p.equals(this.getOwner()) && z == Zone.CREATURE_BOARD) {
			this.attacks = 1;
			System.out.println(getId() + " has updated their attack count.");
		} else {
			// the other player has started their turn.
			if (z == Zone.CREATURE_BOARD) {
				// reset attacks to 0 on opponents turn.
				this.attacks = 0;
			}
		}
		return super.onTurnStart(p, z);
	}

	public void setAttacks(int attacks) {
		this.attacks = attacks;
	}

	public void changeMaxHealthBy(int amount) {
		this.maxHealth += amount;
		this.health += amount;
	}

	public void changeAttackBy(int amount) {
		this.attack += amount;
	}

}
