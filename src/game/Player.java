/**
 * Class to represent a player in the game.
 * @author Raghu
 *
 */
public class Player {
	private int life;
	private int resources;
	private Map<ElementType, Integer> elementMap;
	public Player(int l){
		life = l;
		resources = 0;
		elementMap = new HashMap<ElementType, Integer>();
	}
	
	public int getLife(){
		return life;
	}
	
	public void changeResources (int newCount){
		resources = newCount;
	}
	
	public void setElement(ElementType type, int elem){
		elementMap.put(type, elem);
	}
	
	public int getResources(){
		return resources;
	}
	
	public int getElem(ElementType type){
		int elemCount = elementMap.get(type);
		if(elemCount == null){
			return 0;
		}
		return elemCount;
	}
	
	protected void setLife(int newLife){
		life = newLife
	}
	
	protected void takeDamage(int damage){
		life -= damage;
	}
	
	protected void healDamage(int heal){
		life += heal;
	}
}
