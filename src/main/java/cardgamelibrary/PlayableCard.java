package cardgamelibrary;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import com.google.gson.JsonObject;

import game.Player;

/**
 * A card that can actually be played. Probably all cards subclass this but they
 * don't technically have to.
 *
 * @author Raghu
 *
 */
public class PlayableCard implements Card {
	private ManaPool							cost;
	private String								image;
	private Player								owner;
	private String								name;
	private String								text;
	private boolean								changed;
	private CardType							type;
	private int										id;
	private static AtomicInteger	idGenerator	= new AtomicInteger(0);
	private Set<ElementType> elementTypes;

	public PlayableCard(ManaPool cost, String image, Player owner, String name, String text, CardType type) {
		this.cost = cost;
		this.image = image;
		this.owner = owner;
		this.name = name;
		this.text = text;
		this.type = type;
		// should cards be initialized with a changed of true? YEEN ::: )D
		this.changed = true;
		this.id = idGenerator.incrementAndGet();
		setElementTypes(cost);
	}
	
	private void setElementTypes(ManaPool cost){
		this.elementTypes = new HashSet<ElementType>();
		for(ElementType e : ElementType.values()){
			if(cost.getElement(e) > 0){
				elementTypes.add(e);
			}
		}
	}

	public boolean hasElement(ElementType e){
		return elementTypes.contains(e);
	}
	
	public void addElementType(ElementType e){
		elementTypes.add(e);
	}
	
	// tells us if the card has changed since the last time we sent it to the
	// front end.
	@Override
	public boolean hasChanged() {
		return changed;
	}

	// gives us the owner of the card.
	@Override
	public Player getOwner() {
		return owner;
	}

	// sets hasChanged.
	public void setChange(boolean b) {
		changed = b;
	}

	// changes who owns a card, used for players "taking control" of other cards.
	public void changePlayer(Player p) {
		this.owner = p;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return name;
	}

	@Override
	public ManaPool getCost() {
		// TODO Auto-generated method stub
		return cost;
	}

	@Override
	public int getId() {
		// TODO Auto-generated method stub
		return id;
	}

	@Override
	public String getText() {
		// TODO Auto-generated method stub
		return text;
	}

	@Override
	public String getImage() {
		// TODO Auto-generated method stub
		return image;
	}

	@Override
	public CardType getType() {
		// TODO Auto-generated method stub
		return type;
	}
	
	public boolean equals(Object other){
		if(!(other instanceof Card)){
			return false;
		}
		Card c = (Card) other;
		return this.getId() == c.getId();
	}

	@Override
	public JsonObject jsonifySelfChanged() {
		if (hasChanged()) {
			return jsonifySelf();
		} else {
			JsonObject result = new JsonObject();
			result.addProperty("changed", hasChanged());
			result.addProperty("id", getId());
			return result;
		}
	}

	@Override
	public JsonObject jsonifySelfBack() {
		JsonObject result = new JsonObject();
		result.addProperty("id", getId());
		result.addProperty("type", "back");
		return result;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public void setCost(ManaPool cost) {
		this.cost = cost;
	}

	@Override
	public void setOwner(Player target) {
		this.owner = target;
	}
}
