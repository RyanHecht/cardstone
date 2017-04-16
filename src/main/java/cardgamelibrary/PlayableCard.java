package cardgamelibrary;

import game.PlayerType;

/**
 * A card that can actually be played. Probably all cards subclass this but they
 * don't technically have to.
 *
 * @author 42jpa
 *
 */
public interface PlayableCard extends Card {

	public boolean hasChanged();

	public PlayerType getOwner();
}
