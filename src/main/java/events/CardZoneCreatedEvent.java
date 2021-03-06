package events;

import cardgamelibrary.Card;
import cardgamelibrary.Event;
import cardgamelibrary.EventType;
import cardgamelibrary.Zone;

public class CardZoneCreatedEvent extends Event{

	private Card card;
	private Zone createdLocation;

	public CardZoneCreatedEvent(Card card, Zone createdLocation){
		super();
		this.card = card;
		this.createdLocation = createdLocation;
	}
	
	@Override
	public EventType getType() {
		return EventType.CARD_CREATED;
	}
	
	public Card getCard(){
		return card;
	}
	
	public Zone getLocation(){
		return this.createdLocation;
	}

}
