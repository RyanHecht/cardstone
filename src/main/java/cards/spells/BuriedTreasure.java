package cards.spells;

import cardgamelibrary.Board;
import cardgamelibrary.Card;
import cardgamelibrary.CardType;
import cardgamelibrary.Effect;
import cardgamelibrary.ManaPool;
import cardgamelibrary.OrderedCardCollection;
import cardgamelibrary.SpellCard;
import cardgamelibrary.Zone;
import effects.EmptyEffect;
import game.Player;

public class BuriedTreasure extends SpellCard {
	private static final ManaPool	defaultCost		= new ManaPool(25, 0, 0, 1, 0, 0);
	private static final String		defaultImage	= "images/SandTomb.jpg";
	private static final String		defaultName		= "Buried Tomb";
	private static final String		defaultText		= "Draw the most expensive (highest resource cost) card in your deck.";
	private static final CardType	defaultType		= CardType.SPELL;

	public BuriedTreasure(Player owner) {
		super(defaultCost, defaultImage, owner, defaultName, defaultText, defaultType);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Effect onCardPlayed(Card c, Zone z) {
		// choose randomly from cards w/max cost.
		if (!(c.equals(this))) {
			return EmptyEffect.create();
		}
		return (Board board) -> {
			// get deck of player who played the card.
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
				// need to remove card from deck and add to hand.

				// add card to hand.
				board.addCardToOcc(maxCostCard, board.getOcc(getOwner(), Zone.HAND));
			}
		};
	}

}
