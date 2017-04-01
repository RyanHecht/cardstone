/**
 * Class to represent a player in the game.
 * @author Raghu
 *
 */
public class Player {
	private int life;
	private int resources;
	private int fire;
	private int water;
	private int earth;
	private int air;
	private int balance;
	public Player(int l){
		life = l;
		resources = 0;
		fire = 0;
		water = 0;
		earth = 0;
		air = 0;
		balance = 0;
	}
	
	public int getLife(){
		return life;
	}
	
	protected void setLife(int newLife){
		life = newLife
	}
}
