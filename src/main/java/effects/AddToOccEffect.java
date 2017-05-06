package effects;

import cardgamelibrary.Board;
import cardgamelibrary.Card;
import cardgamelibrary.Effect;
import cardgamelibrary.Zone;
import game.Player;

public class AddToOccEffect implements Effect {

	Card card;
	Player owner;
	Zone end;
	Zone start;
	
	public AddToOccEffect(Card card, Player owner, Zone end, Zone start) {
		super();
		this.card = card;
		this.owner = owner;
		this.end = end;
		this.start = start;
	}
	
	public AddToOccEffect(Card card, Player owner, Zone end, Zone start){
		
	}


	public Card getCard() {
		return card;
	}


	public void setCard(Card card) {
		this.card = card;
	}


	public Player getOwner() {
		return owner;
	}


	public void setOwner(Player owner) {
		this.owner = owner;
	}


	public Zone getEnd() {
		return end;
	}


	public void setEnd(Zone end) {
		this.end = end;
	}


	public Zone getStart() {
		return start;
	}


	public void setStart(Zone start) {
		this.start = start;
	}


	@Override
	public void apply(Board board) {
		board.addCardToOcc(card, board.getOcc(owner, end), board.getOcc(owner, start));
	}

	@Override
	public EffectType getType() {
		return EffectType.CARD_ZONE_CHANGED;
	}

}
