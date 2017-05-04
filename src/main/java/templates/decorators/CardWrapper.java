package templates.decorators;

import com.google.gson.JsonObject;

import cardgamelibrary.Card;
import cardgamelibrary.CardType;
import cardgamelibrary.Creature;
import cardgamelibrary.CreatureInterface;
import cardgamelibrary.Effect;
import cardgamelibrary.ElementType;
import cardgamelibrary.Event;
import cardgamelibrary.ManaPool;
import cardgamelibrary.OrderedCardCollection;
import cardgamelibrary.Zone;
import game.Player;
import templates.PlayerChoosesCards;
import templates.TargetsOtherCard;
import templates.TargetsPlayer;

public class CardWrapper implements Card {

	protected Card internal;

	public CardWrapper(Creature internal) {
		this.internal = internal;
	}

	// Doesn't have to be unique
	@Override
	public String getName() {
		return internal.getName();
	}

	@Override
	public ManaPool getCost() {
		return internal.getCost();
	}

	// Must be unique. We should register all cards somehow and assign ids as we
	// do to prevent conflicting ids
	@Override
	public int getId() {
		return internal.getId();
	}

	// can return an empty string, doesn't hafta be unique.
	@Override
	public String getText() {
		return internal.getText();
	}

	@Override
	public String getImage() {
		return internal.getImage();
	}

	@Override
	public boolean hasChanged() {
		return internal.hasChanged();
	}

	/**
	 * Get the type of the card. Basically will be used instead of putting
	 * instanceof all over the place.
	 *
	 * @return the type of the card.
	 */
	@Override
	public CardType getType() {
		return internal.getType();
	}

	@Override
	public Player getOwner() {
		return internal.getOwner();
	}

	/*
	 * Note: In all the effect producing methods, the zone represents the zone
	 * THIS CARD is in. Some cards will have different behaviors based on where
	 * they are currently. This param will be passed in the OCC method to generate
	 * effects.
	 */

	@Override
	public Effect onPlayerDamage(Player p, Card src, int dmg, Zone z) {
		return internal.onPlayerDamage(p, src, dmg, z);
	}

	@Override
	public Effect onPlayerHeal(Player p, Card src, int heal, Zone z) {
		return internal.onPlayerHeal(p, src, heal, z);
	}

	@Override
	public Effect onTurnStart(Player p, Zone z) {
		return internal.onTurnStart(p, z);
	}

	@Override
	public Effect onTurnEnd(Player p, Zone z) {
		return internal.onTurnEnd(p, z);
	}

	// when something else is damaged. Creatures have a
	// takeDamage method that specifies that they are the thing taking damage.
	@Override
	public Effect onCreatureDamage(CreatureInterface target, Card src, int dmg, Zone z) {
		return internal.onCreatureDamage(target, src, dmg, z);
	}

	@Override
	public Effect onCreatureHeal(CreatureInterface target, Card src, int heal, Zone z) {
		return internal.onCreatureHeal(target, src, heal, z);
	}

	// specific behaviors based on when certain cards are played
	@Override
	public Effect onCardPlayed(Card c, Zone z) {
		return internal.onCardPlayed(c, z);
	}

	@Override
	public boolean onProposedLegalityEvent(Event e, Zone z) {
		return internal.onProposedLegalityEvent(e, z);
	}

	@Override
	public String getComplaint(Event e, Zone z) {
		return internal.getComplaint(e, z);
	}

	@Override
	public boolean onProposedEvent(Event e, Zone z) {
		return internal.onProposedEvent(e, z);
	}

	@Override
	public Event getNewProposition(Event e, Zone z) {
		return internal.getNewProposition(e, z);
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
	@Override
	public Effect onThisPlayed(Card c, Zone z) {
		return internal.onThisPlayed(c, z);
	}

	// when a card is drawn. We can also use this to perform some behavior if
	// THIS card is drawn (i.e. some card auto summons when it's drawn or
	// something.
	@Override
	public Effect onCardDrawn(Card drawn, Zone z) {
		return internal.onCardDrawn(drawn, z);
	}

	// creature dies
	@Override
	public Effect onCreatureDeath(CreatureInterface cr, Zone z) {
		return internal.onCreatureDeath(cr, z);
	}

	// card changes zones
	@Override
	public Effect onZoneChange(Card c, OrderedCardCollection dest, OrderedCardCollection start, Zone z) {
		return internal.onZoneChange(c, dest, start, z);
	}

	// when a card targets another card
	@Override
	public Effect onCardTarget(TargetsOtherCard targetter, Card target, Zone z, Zone targetIn) {
		return internal.onCardTarget(targetter, target, z, targetIn);
	}

	// when a card targets a player
	@Override
	public Effect onPlayerTarget(TargetsPlayer targetter, Player target, Zone z) {
		return internal.onPlayerTarget(targetter, target, z);
	}

	// creature attacks another creature
	@Override
	public Effect onCreatureAttack(CreatureInterface attacker, CreatureInterface target, Zone z) {
		return internal.onCreatureAttack(attacker, target, z);
	}

	// creature attacks player
	@Override
	public Effect onPlayerAttack(CreatureInterface attacker, Player target, Zone z) {
		return internal.onPlayerAttack(attacker, target, z);
	}

	// when a creature's attack is changed.
	// note that amtChange can be negative or positive.
	@Override
	public Effect onAttackChange(CreatureInterface changed, int amtChange, Zone z) {
		return internal.onAttackChange(changed, amtChange, z);
	}

	// when a creature's health is changed.
	// note that amtChange can be negative or positive.
	@Override
	public Effect onHealthChange(CreatureInterface changed, int amtChange, Zone z) {
		return internal.onHealthChange(changed, amtChange, z);
	}

	// when a player gains elemental resources.
	@Override
	public Effect onElementGain(Player player, ElementType elem, int amount, Zone z) {
		return internal.onElementGain(player, elem, amount, z);
	}

	// when cards are chosen by the player through a PlayerChoosesCard situation.
	@Override
	public Effect onCardChosen(PlayerChoosesCards chooser, Card chosen, Zone z) {
		return internal.onCardChosen(chooser, chosen, z);
	}

	@Override
	public JsonObject jsonifySelf() {
		return internal.jsonifySelf();
	}

	@Override
	public JsonObject jsonifySelfChanged() {
		return internal.jsonifySelfChanged();
	}

	@Override
	public Effect onCardZoneCreated(Card card, Zone location, Zone zone) {
		return internal.onCardZoneCreated(card, location, zone);
	}

	@Override
	public JsonObject jsonifySelfBack() {
		return internal.jsonifySelfBack();
	}

	@Override
	public boolean isA(Class<?> c) {
		return this.getClass() == c || internal.isA(c);
	}

}
