package events;

import cardgamelibrary.Card;
import cardgamelibrary.Event;
import cardgamelibrary.EventType;
import cardgamelibrary.Zone;

public class CardZoneCreatedEvent implements Event{

	private Card card;
	private Zone createdLocation;

	public CardZoneCreatedEvent(Card card, Zone createdLocation){
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
