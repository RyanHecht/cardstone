package cards;

import cardgamelibrary.Board;
import cardgamelibrary.Card;
import cardgamelibrary.CardType;
import cardgamelibrary.ConcatEffect;
import cardgamelibrary.Creature;
import cardgamelibrary.Effect;
import cardgamelibrary.ManaPool;
import cardgamelibrary.OrderedCardCollection;
import cardgamelibrary.Zone;
import effects.AddToOccCrossPlayerEffect;
import effects.EffectMaker;
import game.Player;

public class CarelessScribe extends Creature{

	private static final String defaultImage = "images/CarelessScribe.jpg";
	private static final String defaultName = "Careless Scribe";
	private static final String defaultText = "Swap two cards between yours and your opponents decks. The two cards swap costs and names.";
	private static final int defaultHealth = 3;
	private static final int defaultAttack = 3;
	private static final CardType defaultType = CardType.CREATURE;

	public CarelessScribe(Player owner) {
		super(defaultAttack, defaultHealth, new ManaPool(30, 0, 2, 0, 0, 0), defaultImage, owner, defaultName, defaultText, defaultType);
	}
	
	public Effect onThisPlayed(Card c, Zone z){
		return new EffectMaker((Board b) -> {
			ConcatEffect ce = new ConcatEffect(this);
			OrderedCardCollection myDeck = b.getOcc(getOwner(), Zone.DECK);
			OrderedCardCollection otherDeck = b.getOcc(b.getOpposingPlayer(getOwner()), Zone.DECK);
			if(myDeck.size() > 0 && otherDeck.size() > 0){
				Card first = myDeck.getRandomCard();
				Card second = otherDeck.getRandomCard();
				String interName = first.getName();
				first.setName(second.getName());
				second.setName(interName);
				ManaPool intercost = first.getCost();
				first.setCost(second.getCost());
				second.setCost(intercost);
				ce.addEffect(new AddToOccCrossPlayerEffect(first,first.getOwner(),
						b.getOpposingPlayer(first.getOwner()),Zone.DECK,Zone.DECK,this));
				ce.addEffect(new AddToOccCrossPlayerEffect(second,second.getOwner(),
						b.getOpposingPlayer(second.getOwner()),Zone.DECK,Zone.DECK,this));
			}
			return ce;
		},c);
	}
	
}
