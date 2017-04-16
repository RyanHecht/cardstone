package game;

import cardgamelibrary.ElementType;
import cardgamelibrary.ManaPool;

/**
 * Class to represent a player in the game.
 *
 * @author Raghu
 *
 */
public class Player {
	private int				life;
	private ManaPool	manaPool;

	public Player(int l) {
		life = l;
		manaPool = new ManaPool(0, 0, 0, 0, 0, 0);
	}

	public int getLife() {
		return life;
	}

	public void changeResources(int newCount) {
		manaPool.setResources(newCount);
	}

	public void setElement(ElementType type, int elem) {
		manaPool.setElement(type, elem);
	}

	public int getResources() {
		return manaPool.getResources();
	}

	public int getElem(ElementType type) {
		return manaPool.getElement(type);
	}

	protected void setLife(int newLife) {
		life = newLife;
	}

	protected void takeDamage(int damage) {
		life -= damage;
	}

	protected void healDamage(int heal) {
		life += heal;
	}

	public boolean validateCost(ManaPool cost) {
		return manaPool.canPay(cost);
	}
}
