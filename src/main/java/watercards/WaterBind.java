package watercards;

import java.lang.annotation.ElementType;

import cardgamelibrary.Board;
import cardgamelibrary.Card;
import cardgamelibrary.CardType;
import cardgamelibrary.ConcatEffect;
import cardgamelibrary.Effect;
import cardgamelibrary.Element;
import cardgamelibrary.Event;
import cardgamelibrary.EventType;
import cardgamelibrary.ManaPool;
import cardgamelibrary.SpellCard;
import cardgamelibrary.Zone;
import devotions.TideLevel;
import devotions.WaterDevotion;
import events.CardPlayedEvent;
import game.Player;
import templates.ActiveOneTurnCard;

public class WaterBind extends SpellCard implements ActiveOneTurnCard{

	private static final String defaultImage = "images/WaterBind.jpg";
	private static final String defaultName = "Water Bind";
	private static final String defaultText = "Steal 2 element from your opponents pool. Low tide: also prevent them from playing element card next turn.";
	private static final CardType defaultType = CardType.SPELL;
	private int value;

	public WaterBind(Player owner) {
		super(new ManaPool(40, 0, 1, 0, 0, 0), defaultImage, owner, defaultName, defaultText, defaultType);
		value = 0;
	}
	
	public Effect onThisPlayed(Card c, Zone z){
		effects.EffectMaker em = new effects.EffectMaker((Board b) -> {
			ConcatEffect ce = new ConcatEffect(this);
			Player opponent = b.getOpposingPlayer(getOwner());
			ManaPool cost = new ManaPool(0,0,0,0,0,0);
			for(cardgamelibrary.ElementType e : cardgamelibrary.ElementType.values()){
				cost.setElement(e, opponent.getElem(e));
			}
			return ce;
		},this);
		if(WaterDevotion.getLevelOfWater(getOwner().getDevotion()).equals(TideLevel.LOW)){
			value = getInitValue();
		}
		return em;
	}

	@Override
	public void decrementValue() {
		value--;
	}

	@Override
	public int getValue() {
		return value;
	}

	@Override
	public void setValue(int value) {
		this.value = value;
	}

	@Override
	public int getInitValue() {
		return 2;
	}
	
	public boolean onProposedLegalityEvent(Event e, Zone z) {
		if(isActive()){
			if(e.getType().equals(EventType.CARD_PLAYED)){
				CardPlayedEvent cpe = (CardPlayedEvent) e;
				if(cpe.getCard().isA(Element.class)){
					return true;
				}
			}
		}
		return false;
	}

	public String getComplaint(Event e, Zone z) {
		return "Can't play element this turn because of opposing water bind!";
	}
	
}
