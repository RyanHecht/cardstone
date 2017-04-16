package cardgamelibrary;

import game.Player;

/**
 * A card that can actually be played. Probably all cards subclass this but they
 * don't technically have to.
 *
 * @author 42jpa
 *
 */
public class PlayableCard implements Card {
	private ManaPool	cost;
	private String		image;
	private Player		owner;
	private String		name;
	private String		text;
	private boolean		changed;
	private CardType	type;
	private int				id;

	public PlayableCard() {

	}

	// tells us if the card has changed since the last time we sent it to the
	// front end.
	public boolean hasChanged() {
		return changed;
	}

	// gives us the owner of the card.
	public Player getOwner() {
		return owner;
	}

	// resets hasChanged.
	public void resetChange() {
		changed = false;
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
}
