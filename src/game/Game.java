/**
 * Class to represent a game.
 * @author Raghu
 *
 */
public class Game {
	private Board board;
	private Player playerOne;
	private Player playerTwo;

	
	public Game(){
		// Initialize both players @30 life.
		playerOne = new Player(30);
		playerTwo = new Player(30);
		
		// Some sort of board constructor goes here.
		Board = new Board();
	}
	
	public void startGame(){
		while(playerOne.getLife() > 0 && playerTwo.getLife() > 0){
			// if neither player has 0 life the game goes on.
			
		}
		if(playerOne.getLife() <= 0 && playerTwo.getLife() > 0){
			System.out.println("Player One loses.");
		} else if (playerTwo.getLife() <= 0 && playerOne.getLife() > 0){
			System.out.println("Player Two loses.");
		} else {
			System.out.println("Game is drawn.");
		}
	}
}
