package cardgamelibrary;

import com.google.gson.JsonObject;

import game.Player;

/**
 * Class that implements the card interface. Not sure if this will be needed or
 * not.
 *
 * @author Raghu
 *
 */
public class BaseCard implements Card {
	private ManaPool	cost;
	private String		image;
	private String		name;
	private String		text;
	private boolean		changed;
	private CardType	type;

	public BaseCard(ManaPool cost, String image, String name, String text, CardType type) {
		this.cost = cost;
		this.image = image;
		this.name = name;
		this.text = text;
		this.type = type;
		this.changed = false;
	}

	/**
	 * Converts a BaseCard into a playable card.
	 *
	 * @param b
	 *          the BaseCard we want to convert.
	 * @param owner
	 *          the owner to assign the card to.
	 * @return the newly created PlayableCard!
	 */
	public static PlayableCard makePlayableCard(BaseCard b, Player owner) {
		// the problem with this is that we can't return the specific instances of
		// playable card that we want (i.e. SkyWhale).
		PlayableCard p = new PlayableCard(b.getCost(), b.getImage(), owner, b.getName(), b.getText(), b.getType());
		return p;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return name;
	}

	@Override
	public ManaPool getCost() {
		return cost;
	}

	@Override
	public int getId() {
		return 0;
	}

	@Override
	public String getText() {
		return text;
	}

	@Override
	public String getImage() {
		return image;
	}

	@Override
	public boolean hasChanged() {
		return changed;
	}

	@Override
	public CardType getType() {
		return type;
	}

	@Override
	public JsonObject jsonifySelfChanged() {
		return null;
	}

}
