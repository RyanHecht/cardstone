package cards;

import java.util.List;

import cardgamelibrary.Board;
import cardgamelibrary.Card;
import cardgamelibrary.CardType;
import cardgamelibrary.Effect;
import cardgamelibrary.ManaPool;
import cardgamelibrary.SpellCard;
import cardgamelibrary.Zone;
import cards.templates.PlayerChoosesCards;
import game.Player;

public class DelveTheDepths extends SpellCard implements PlayerChoosesCards {

	private static final ManaPool	defaultCost		= new ManaPool(25, 0, 3, 0, 0, 0);
	private static final String		defaultImage	= "images/DelveTheDepts.jpg";
	private static final String		defaultName		= "Delve The Depths";
	private static final String		defaultText		= "Select any card from your deck and add it to your hand.";
	private static final CardType	defaultType		= CardType.SPELL;

	public DelveTheDepths(Player owner) {
		super(defaultCost, defaultImage, owner, defaultName, defaultText, defaultType);
		// TODO Auto-generated constructor stub
	}

	@Override
	public List<Card> getOptions(Board board) {
		// since this card searches your deck, the options list is your whole deck!
		return board.getOcc(getOwner(), Zone.DECK).getCards();
	}

	@Override
	public Effect getChooseEffect(PlayerChoosesCards thisCard, Card chosen) {
		return (Board board) -> {
			// add chosen card to hand.
			board.addCardToOcc(chosen, board.getOcc(getOwner(), Zone.HAND), board.getOcc(getOwner(), Zone.DECK));
		};
	}
}
