package cardgamelibrary;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import effects.EmptyEffect;
import game.Player;
import templates.PlayerChoosesCards;
import templates.TargetsOtherCard;
import templates.TargetsPlayer;

/**
 * A card in the game.
 *
 * @author 42jpa
 *
 */
public interface Card extends Jsonifiable, Serializable {

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
	 * @return the type of the card.
	 */
	CardType getType();

	Player getOwner();

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

	default public Effect onTurnStart(Player p, Zone z) {
		return EmptyEffect.create();
	}

	default public Effect onTurnEnd(Player p, Zone z) {
		return EmptyEffect.create();
	}

	// when something else is damaged. Creatures have a
	// takeDamage method that specifies that they are the thing taking damage.
	default public Effect onCreatureDamage(Creature target, Card src, int dmg, Zone z) {
		return EmptyEffect.create();
	}

	default public Effect onCreatureHeal(Creature target, Card src, int heal, Zone z) {
		return EmptyEffect.create();
	}

	// specific behaviors based on when certain cards are played
	default public Effect onCardPlayed(Card c, Zone z) {
		// cards that have effects that trigger when THEY are played activate stuff
		// via this.
		if (c.equals(this) && z == Zone.HAND) {
			// pay cost of the card.
			getOwner().payCost(getCost());
			// return effect specific to this card being played!
			return onThisPlayed(c, z);
		}
		return EmptyEffect.create();
	}

	default public boolean onProposedLegalityEvent(Event e, Zone z) {
		return false;
	}

	public default String getComplaint(Event e, Zone z) {
		return "Invalid Event";
	}

	default public boolean onProposedEvent(Event e, Zone z) {
		return false;
	}

	default public Event getNewProposition(Event e, Zone z) {
		return e;
	}

	/**
	 * Specific behaviors based on when THIS specific card is played.
	 *
	 * @param c
	 *          this card!
	 * @param z
	 *          the zone this card is in.
	 * @return an effect this card produces when it's played.
	 */
	default public Effect onThisPlayed(Card c, Zone z) {
		// make sure this only triggers if c really is this card.
		// and if the card is in your hand.
		assert (c.equals(this) && z == Zone.HAND);
		return EmptyEffect.create();
	}

	// when a card is drawn. We can also use this to perform some behavior if
	// THIS card is drawn (i.e. some card auto summons when it's drawn or
	// something.
	default public Effect onCardDrawn(Card drawn, Zone z) {
		return EmptyEffect.create();
	}

	// creature dies
	default public Effect onCreatureDeath(Creature cr, Zone z) {
		return EmptyEffect.create();
	}

	// card changes zones
	default public Effect onZoneChange(Card c, OrderedCardCollection dest, OrderedCardCollection start, Zone z) {
		return EmptyEffect.create();
	}

	// when a card targets another card
	default public Effect onCardTarget(TargetsOtherCard targetter, Card target, Zone z, Zone targetIn) {
		return EmptyEffect.create();
	}

	// when a card targets a player
	default public Effect onPlayerTarget(TargetsPlayer targetter, Player target, Zone z) {
		return EmptyEffect.create();
	}

	// creature attacks another creature
	default public Effect onCreatureAttack(Creature attacker, Creature target, Zone z) {
		return EmptyEffect.create();
	}

	// creature attacks player
	default public Effect onPlayerAttack(Creature attacker, Player target, Zone z) {
		return EmptyEffect.create();
	}

	// when a creature's attack is changed.
	// note that amtChange can be negative or positive.
	default public Effect onAttackChange(Creature changed, int amtChange, Zone z) {
		return EmptyEffect.create();
	}

	// when a creature's health is changed.
	// note that amtChange can be negative or positive.
	default public Effect onHealthChange(Creature changed, int amtChange, Zone z) {
		return EmptyEffect.create();
	}

	// when a player gains elemental resources.
	default public Effect onElementGain(Player player, ElementType elem, int amount, Zone z) {
		return EmptyEffect.create();
	}

	// when cards are chosen by the player through a PlayerChoosesCard situation.
	default public Effect onCardChosen(PlayerChoosesCards chooser, Card chosen, Zone z) {
		return EmptyEffect.create();
	}

	@Override
	default public JsonObject jsonifySelf() {
		JsonObject result = new JsonObject();
		result.addProperty("text", this.getText());
		result.addProperty("id", this.getId());
		result.addProperty("name", this.getName());
		result.addProperty("image", this.getImage());
		result.addProperty("changed", hasChanged());
		result.add("cost", this.getCost().jsonifySelf());
		result.addProperty("type", "none");
		return result;
	}

	@Override
	JsonObject jsonifySelfChanged();

	default public JsonObject jsonifySelfWithZone(Zone inThisZone) {
		List<String> states = new LinkedList<>();
		JsonObject result = jsonifySelf();

		Gson gson = new Gson();

		if (this.is(Creature.class)) {
			if (((Creature) this).getNumAttacks() > 0) {
				states.add("canAttack");
			}
		}

		result.addProperty("states", gson.toJson(states));

		return result;
	}

	/**
	 * Checks to see if a given card is another class (will be used to check
	 * wrappers).
	 *
	 * @param c
	 *          the class we are checking.
	 * @return a boolean representing whether the given card is another class.
	 */
	default public boolean is(Class c) {
		return this.getClass() == c;
	}

	default public Effect onCardZoneCreated(Card card, Zone location, Zone zone) {
		return EmptyEffect.create();
	}

	JsonObject jsonifySelfBack();
}
