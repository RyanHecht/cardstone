package cardgamelibrary;

import effects.EmptyEffect;
import game.Player;

public class AuraCard extends PlayableCard {

	public AuraCard(ManaPool cost, String image, Player owner, String name, String text, CardType type) {
		super(cost, image, owner, name, text, type);
	}

	@Override
	public Effect onCardPlayed(Card c, Zone z) {
		// cards that have effects that trigger when THEY are played activate stuff
		// via this.
		if (c.equals(this) && z == Zone.HAND) {
			// pay cost of the card.
			getOwner().payCost(getCost());

			ConcatEffect effect = new ConcatEffect();
			effect.addEffect((Board board) -> {
				// effect to move aura to aura board from hand.
				board.addCardToOcc(this, board.getOcc(getOwner(), Zone.AURA_BOARD), board.getOcc(getOwner(), Zone.HAND));
			});

			// add any specific effects for this aura being played.
			effect.addEffect(onThisPlayed(c, z));
			return effect;
		}
		return EmptyEffect.create();
	}

}
