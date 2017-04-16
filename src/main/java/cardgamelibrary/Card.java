package cardgamelibrary;

import game.Player;

/**
 * A card in the game.
 *
 * @author 42jpa
 *
 */
public interface Card {

	// Doesn't have to be unique
	String getName();

	// Must be unique. We should register all cards somehow and assign ids as we
	// do to prevent conflicting ids
	int getId();

	// can return an empty string, doesn't hafta be unique.
	String getText();

	String getImage();
	// flyweight getting thing for all the image for the given name by ID

	/**
	 * Get the type of the card. Basically will be used instead of putting
	 * instanceof all over the place.
	 *
	 * @return
	 */
	CardType getType();

	public Effect onPlayerDamage(Player p, Card src);

	public Effect onPlayerHeal(Player p, Card src);

	public Effect onTurnEnd();

	// when something else is damaged. Creatures have a
	// takeDamage method that specifies that they are the thing taking damage.
	public Effect onDamage(Creature target, Card src);

	public Effect onHeal(Creature target, Card src);

	// specific behaviors based on when certain cards are played
	public Effect cardPlayed(Card c);

	// when a card is drawn. We can also use this to perform some behavior if
	// THIS card is drawn (i.e. some card auto summons when it's drawn or
	// something.
	public Effect cardDrawn(Card drawn);

	public Effect creatureDied(Creature cr);

}
