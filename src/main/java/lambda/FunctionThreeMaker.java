package lambda;

import cardgamelibrary.Board;
import cardgamelibrary.CreatureInterface;
import cardgamelibrary.FunctionThree;
import game.Player;

/**
 * Class used to generate FunctionThrees that are commonly used in code (i.e.
 * AoeDamageEffect functions).
 * 
 * @author Raghu
 *
 */
public final class FunctionThreeMaker {

	// no initialization allowed!
	private FunctionThreeMaker() {

	}

	/**
	 * This will create a functionThree that returns true for all
	 * creatureInterfaces that are fed into it.
	 * 
	 * @return a functionthree that returns true for all creatureInterfaces fed
	 *         into it.
	 */
	public static FunctionThree<Board, CreatureInterface, Boolean> targetsAllCreatures() {
		return (Board b, CreatureInterface cr) -> {
			return true;
		};
	}

	/**
	 * Generates function to only target creatures belonging to a specific player.
	 * 
	 * @param p
	 *          the player whose creatures we are targeting.
	 * @return a functionthree that returns true for CreatureInterfaces that
	 *         belong to p.
	 */
	public static FunctionThree<Board, CreatureInterface, Boolean> targetsPlayerCreatures(Player p) {
		return (Board b, CreatureInterface cr) -> {
			return cr.getOwner().equals(p);
		};
	}

	/**
	 * Generates a functionthree to return a constant value for all
	 * CreatureInterfaces it is passed.
	 * 
	 * @param dmg
	 *          the constant value to return.
	 * @return see the first line.
	 */
	public static FunctionThree<Board, CreatureInterface, Integer> determineCreatureDamage(Integer dmg) {
		return (Board b, CreatureInterface cr) -> {
			return dmg;
		};
	}
}
