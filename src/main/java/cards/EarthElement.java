package cards;

import cardgamelibrary.Board;
import cardgamelibrary.Card;
import cardgamelibrary.CardType;
import cardgamelibrary.Effect;
import cardgamelibrary.Element;
import cardgamelibrary.ElementType;
import cardgamelibrary.ManaPool;
import cardgamelibrary.Zone;
import game.Player;

public class EarthElement extends Element {

	private static final ManaPool defaultCost = ManaPool.emptyPool();
	private static final String defaultImage = "images/waterChoices/fireBig.jpg";
	private static final String defaultName = "earth";
	private static final String defaultText = "";
	private static final CardType defaultType = CardType.ELEMENT;

	public EarthElement(Player owner) {
		super(defaultCost, defaultImage, owner, defaultName, defaultText, defaultType);
	}

	@Override
	public Effect onThisPlayed(Card c, Zone z) {
		assert (this.equals(c));
		return (Board board) -> {
			// give player element.
			board.givePlayerElement(getOwner(), ElementType.EARTH, this.DEFAULT_ELEMENT_GAIN);
			// send element to grave from hand.
			board.addCardToOcc(c, board.getOcc(getOwner(), Zone.GRAVE), board.getOcc(getOwner(), Zone.HAND));
		};
	}

}
