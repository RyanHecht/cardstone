package cardgamelibrary;

import com.google.gson.JsonObject;

import effects.EmptyEffect;
import game.Player;

/**
 * A card in the game.
 *
 * @author 42jpa
 *
 */
public interface Card extends Jsonifiable {

	// Doesn't have to be unique
	String getName();

	ManaPool getCost();

	// Must be unique. We should register all cards somehow and assign ids as we
	// do to prevent conflicting ids
	int getId();

	// can return an empty string, doesn't hafta be unique.
	String getText();

	String getImage();
	// flyweight getting thing for all the image for the given name by ID

	boolean hasChanged();

	/**
	 * Get the type of the card. Basically will be used instead of putting
	 * instanceof all over the place.
	 *
	 * @return
	 */
	CardType getType();

	/*
	 * Note: In all the default effect producing methods, the zone represents the
	 * zone THIS CARD is in. Some cards will have different behaviors based on
	 * where they are currently. This param will be passed in the OCC method to
	 * generate effects.
	 */

	default public Effect onPlayerDamage(Player p, Card src, int dmg, Zone z) {
		return EmptyEffect.create();
	}

	default public Effect onPlayerHeal(Player p, Card src, int heal, Zone z) {
		return EmptyEffect.create();
	}

	default public Effect onTurnEnd(Zone z) {
		return EmptyEffect.create();
	}

	// when something else is damaged. Creatures have a
	// takeDamage method that specifies that they are the thing taking damage.
	default public Effect onDamage(Creature target, Card src, int dmg, Zone z) {
		return EmptyEffect.create();
	}

	default public Effect onHeal(Creature target, Card src, int heal, Zone z) {
		return EmptyEffect.create();
	}

	// specific behaviors based on when certain cards are played
	default public Effect cardPlayed(Card c, Zone z) {
		return EmptyEffect.create();
	}

	// when a card is drawn. We can also use this to perform some behavior if
	// THIS card is drawn (i.e. some card auto summons when it's drawn or
	// something.
	default public Effect cardDrawn(Card drawn, Zone z) {
		return EmptyEffect.create();
	}

	default public Effect creatureDied(Creature cr, Zone z) {
		return EmptyEffect.create();
	}

	default public Effect onZoneChange(Card c, OrderedCardCollection start, OrderedCardCollection dest, Zone z) {
		return EmptyEffect.create();
	}

	default JsonObject jsonifySelf() {
		JsonObject result = new JsonObject();
		result.addProperty("text", this.getText());
		result.addProperty("id", this.getId());
		result.addProperty("name", this.getName());
		result.addProperty("image", this.getImage());
		result.addProperty("changed", hasChanged());
		result.add("cost", this.getCost().jsonifySelf());
		result.addProperty("type", "creature");
		return result;
	}

	JsonObject jsonifySelfChanged();
}
