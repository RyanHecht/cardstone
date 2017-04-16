package cardgamelibrary;

import effects.EmptyEffect;
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

	default public Effect onPlayerDamage(Player p, Card src, int dmg) {
		return EmptyEffect.create();
	}

	default public Effect onPlayerHeal(Player p, Card src, int heal) {
		return EmptyEffect.create();
	}

	default public Effect onTurnEnd() {
		return EmptyEffect.create();
	}

	// when something else is damaged. Creatures have a
	// takeDamage method that specifies that they are the thing taking damage.
	default public Effect onDamage(Creature target, Card src, int dmg) {
		return EmptyEffect.create();
	}

	default public Effect onHeal(Creature target, Card src, int heal) {
		return EmptyEffect.create();
	}

	// specific behaviors based on when certain cards are played
	default public Effect cardPlayed(Card c) {
		return EmptyEffect.create();
	}

	// when a card is drawn. We can also use this to perform some behavior if
	// THIS card is drawn (i.e. some card auto summons when it's drawn or
	// something.
	default public Effect cardDrawn(Card drawn) {
		return EmptyEffect.create();
	}

	default public Effect creatureDied(Creature cr) {
		return EmptyEffect.create();
	}

	default public Effect onZoneChange(Card c, OrderedCardCollection start, OrderedCardCollection dest) {
		return EmptyEffect.create();
	}
}
