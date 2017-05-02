package cardgamelibrary;

public enum EventType {

	// etc. The nice thing here is we can add them whenever we want, without
	// causing any problematic behavior for events that already exist.
	EVENT_CANCELLED, CARD_PLAYED, TURN_START, TURN_END, CARD_DAMAGED, PLAYER_DAMAGED, CARD_DRAWN, CREATURE_DIED, PLAYER_ATTACKED, CREATURE_ATTACKED, CARD_CREATED, HEALTH_CHANGE, ATTACK_CHANGE, PLAYER_HEALED, CARD_HEALED, CARD_TARGETED, CARD_ZONE_CHANGED, ELEMENT_GAINED, CARD_CHOSEN, PLAYER_TARGETED, EMPTY,
}
