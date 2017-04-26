package logins;

/**
 * A simple class to display info about a game
 * to a user.
 * @author wriley1
 */
public class MetaGame {
	private final int id;
	private final int winner;
	private final int moves;
	
	public MetaGame(int id, int winner, int moves) {
		this.id = id;
		this.winner = winner;
		this.moves = moves;
	}
	
	public int getId() {
		return id;
	}
	
	public int getWinner() {
		return winner;
	}
	
	public int getMoves() {
		return moves;
	}
}
