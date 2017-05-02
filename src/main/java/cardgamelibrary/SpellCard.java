package cardgamelibrary;

import com.google.gson.JsonObject;

import effects.EmptyEffect;
import game.Player;

public class SpellCard extends PlayableCard {

	public SpellCard(ManaPool cost, String image, Player owner, String name, String text, CardType type) {
		super(cost, image, owner, name, text, type);
	}

	@Override
	public JsonObject jsonifySelf() {
		JsonObject result = super.jsonifySelf();
		result.addProperty("type", "spell");
		return result;
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
				// effect to move spell to grave from hand.
				board.addCardToOcc(this, board.getOcc(getOwner(), Zone.GRAVE), board.getOcc(getOwner(), Zone.HAND));
			});

			// add any specific effects for this spell being played.
			effect.addEffect(onThisPlayed(c, z));
			return effect;
		}
		return EmptyEffect.create();
	}

}
