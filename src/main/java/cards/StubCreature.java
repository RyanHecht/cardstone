package cards;

import java.util.List;

import cardgamelibrary.CardType;
import cardgamelibrary.Creature;
import cardgamelibrary.EventHandler;

/**
 * Stub creature class that will be used to test! Implements default isDead
 * method.
 *
 * @author Raghu
 *
 */
public class StubCreature implements Creature {

	private int			health	= 20;
	private int			attack	= 10;
	private String	name		= "Naghu Rimmagadda";
	private String	text		= "sup BIATCH";

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

}
