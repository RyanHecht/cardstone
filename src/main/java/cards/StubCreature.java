package cards;

import java.util.List;

import cardgamelibrary.Card;
import cardgamelibrary.CardType;
import cardgamelibrary.Creature;
import cardgamelibrary.EventHandler;
import game.Player;

/**
 * Stub creature class that will be used to test! Implements default isDead
 * method.
 *
 * @author Raghu
 *
 */
public class StubCreature implements Creature {
	private int			maxHealth	= 20;
	private int			health		= 20;
	private int			attack		= 10;
	private String	name			= "Naghu Rimmagadda";
	private String	text			= "yo";

	public StubCreature() {

	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return name;
	}

	@Override
	public int getId() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getText() {
		// TODO Auto-generated method stub
		return text;
	}

	@Override
	public String getImage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CardType getType() {
		// TODO Auto-generated method stub
		return CardType.CREATURE;
	}

	@Override
	public List<EventHandler> getHandlers() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getHealth() {
		// TODO Auto-generated method stub
		return health;
	}

	@Override
	public int getAttack() {
		// TODO Auto-generated method stub
		return attack;
	}

	@Override
	public void setHealth(int newHealth) {
		health = newHealth;

	}

	@Override
	public void onPlayerDamage(Player p, Card src) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPlayerHeal(Player p, Card src) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTurnEnd() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onDamage(Creature target, Card src) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onHeal(Creature target, Card src) {
		// TODO Auto-generated method stub

	}

	@Override
	public void cardPlayed(Card c) {
		// TODO Auto-generated method stub

	}

	@Override
	public void cardDrawn(Card drawn) {
		// TODO Auto-generated method stub

	}

	@Override
	public void creatureDied(Creature cr) {
		// TODO Auto-generated method stub

	}

	@Override
	public int getMaxHealth() {
		// TODO Auto-generated method stub
		return maxHealth;
	}

}
