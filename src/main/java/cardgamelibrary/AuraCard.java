package cardgamelibrary;

import game.Player;

public class AuraCard extends PlayableCard {

	public AuraCard(ManaPool cost, String image, Player owner, String name, String text, CardType type) {
		super(cost, image, owner, name, text, type);
	}
	
	@Override
	public Effect onThisPlayed(Card c, Zone z) {
		return (Board board) -> {
			// note that cost payment is handled in default onCardPlayed so no need to
			// do it here!

			// play the creature onto the board!
			board.addCardToOcc(this, board.getOcc(getOwner(), Zone.CREATURE_BOARD), board.getOcc(getOwner(), Zone.HAND));
		};
	}

}
