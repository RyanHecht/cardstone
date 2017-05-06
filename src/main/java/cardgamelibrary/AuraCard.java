package cardgamelibrary;

import effects.EmptyEffect;
import game.Player;

public class AuraCard extends PlayableCard implements AuraInterface {

	public AuraCard(ManaPool cost, String image, Player owner, String name, String text, CardType type) {
		super(cost, image, owner, name, text, type);
	}
	
	@Override
	public Effect onCardPlayed(Card c, Zone z) {
		// cards that have effects that trigger when THEY are played activate stuff
		// via this.
		ConcatEffect effect = new ConcatEffect();
		if (c.equals(this) && z == Zone.HAND) {
			// pay cost of the card.
			getOwner().payCost(getCost());
			effect.addEffect((Board board) -> {
				// effect to move aura to aura board from hand.
				board.addCardToOcc(this, board.getOcc(getOwner(), Zone.AURA_BOARD), board.getOcc(getOwner(), Zone.HAND));
			});

			// add any specific effects for this aura being played.
			effect.addEffect(onThisPlayed(c, z));
		}
		else{
			effect.addEffect(this.onOtherCardPlayed(c, z));
		}
		return EmptyEffect.create();
	}

}
