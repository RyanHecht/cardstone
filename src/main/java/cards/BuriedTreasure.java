package cards;

import cardgamelibrary.Board;
import cardgamelibrary.Card;
import cardgamelibrary.CardType;
import cardgamelibrary.ConcatEffect;
import cardgamelibrary.Effect;
import cardgamelibrary.ManaPool;
import cardgamelibrary.OrderedCardCollection;
import cardgamelibrary.SpellCard;
import cardgamelibrary.Zone;
import effects.AddToOccEffect;
import effects.EffectMaker;
import effects.EmptyEffect;
import effects.GateEffect;
import game.Player;

public class BuriedTreasure extends SpellCard {
	private static final ManaPool	defaultCost		= new ManaPool(25, 0, 0, 1, 0, 0);
	private static final String		defaultImage	= "images/SandTomb.jpg";
	private static final String		defaultName		= "Buried Treasure";
	private static final String		defaultText		= "Draw the most expensive (highest gold cost) card in your deck.";
	private static final CardType	defaultType		= CardType.SPELL;

	public BuriedTreasure(Player owner) {
		super(defaultCost, defaultImage, owner, defaultName, defaultText, defaultType);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Effect onThisPlayed(Card c, Zone z) {
		// check card is indeed this card.
		assert (c.equals(this));
		ConcatEffect effect = new ConcatEffect();
		effect.addEffect(new EffectMaker((Board board) -> {
			OrderedCardCollection deck = board.getOcc(getOwner(), Zone.DECK);
			if (deck.size() != 0) {
				// if the deck size isn't 0 we should search for a card.
				Card maxCostCard = null;
				for (Card card : deck) {
					if (maxCostCard == null) {
						maxCostCard = card;
					} else {
						if (card.getCost().getResources() > maxCostCard.getCost().getResources()) {
							maxCostCard = card;
						}
					}
				}

				return new AddToOccEffect(maxCostCard,maxCostCard.getOwner(),Zone.HAND,Zone.DECK);
			}
			return EmptyEffect.create();
		}));
		return effect;
	}

}
