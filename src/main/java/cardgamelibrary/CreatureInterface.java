package cardgamelibrary;

import com.google.gson.JsonObject;

import effects.CardDamageEffect;
import effects.EmptyEffect;
import effects.PlayerDamageEffect;
import game.Player;

public interface CreatureInterface extends Card{

	

	public int getHealth() ;

	public int getMaxHealth() ;

	public int getAttack() ;

	public int getNumAttacks() ;

	/**
	 * Tells you if a given creature can still attack.
	 *
	 * @return a boolean representing whether or not the creature can still
	 *         attack.
	 */
	public boolean canAttack() ;

	public void takeDamage(int damage, Card src) ;

	public void heal(int heal, Card src) ;

	public void setHealth(int newHealth) ;

	/**
	 * gets whether the creature is dead. By default just returns if health < 0,
	 * but of course can be overridden.
	 *
	 * @return a boolean representing whether the creature is dead or not.
	 */
	public boolean isDead() ;

	@Override
	public Effect onCardPlayed(Card c, Zone z) ;

	@Override
	public Effect onCreatureAttack(Creature attacker, Creature target, Zone z) ;

	@Override
	public Effect onPlayerAttack(Creature attacker, Player target, Zone z) ;

	@Override
	public JsonObject jsonifySelf() ;

	@Override
	public Effect onTurnStart(Player p, Zone z) ;

	public void setAttacks(int attacks) ;

	public void changeMaxHealthBy(int amount) ;

	public void changeAttackBy(int amount) ;
}
