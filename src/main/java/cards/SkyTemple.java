package cards;

import java.util.LinkedList;
import java.util.List;

import cardgamelibrary.AuraCard;
import cardgamelibrary.Board;
import cardgamelibrary.Card;
import cardgamelibrary.CardType;
import cardgamelibrary.Effect;
import cardgamelibrary.ElementType;
import cardgamelibrary.ManaPool;
import cardgamelibrary.Zone;
import effects.AddToOccEffect;
import effects.CardDrawEffect;
import effects.EffectMaker;
import effects.EffectType;
import effects.RequestCardChooseEffect;
import game.Player;
import templates.ChooseResponderCard;

public class SkyTemple extends AuraCard implements ChooseResponderCard{

	private static final String defaultImage = "images/SkyTemple.jpg";
	private static final String defaultName = "Sky Temple";
	private static final String defaultText = "Whenever you would draw a card on your turn, instead search your deck for an air card or air element and add it to your hand.";
	private static final CardType defaultType = CardType.AURA;

	public SkyTemple(Player owner) {
		super(new ManaPool(20, 0, 0, 2, 0, 0), defaultImage, owner, defaultName, defaultText, defaultType);
	}
	
	public boolean onProposedEffect(Effect e, Zone z, Board b){
		if(z.equals(Zone.AURA_BOARD)){
			if(e.getType().equals(EffectType.CARD_DRAWN)){
				CardDrawEffect cde = (CardDrawEffect)e;
				if(cde.getTarget().equals(getOwner())){
					if(b.getActivePlayer().equals(getOwner())){
						return true;
					}
				}
			}
		}
		return false;
	}
	
	public Effect getNewProposition(Effect e, Zone z){
		return new EffectMaker((Board b) -> {
			List<Card> options = new LinkedList<>();
			for(Card c : b.getOcc(getOwner(), Zone.DECK)){
				if(c.hasElement(ElementType.AIR) || c.isA(AirElement.class)){
					options.add(c);
				}
			}
			if(options.size() != 0){
				return new RequestCardChooseEffect(options,getOwner(),this,this);
			}
			else{
				return e;
			}
		},this);
	}
	
	
	public Effect getChooseEffect(ChooseResponderCard chooser, Card chosenCard) {
		return new AddToOccEffect(chosenCard,getOwner(),Zone.HAND,Zone.DECK,this);
	}
	
}
