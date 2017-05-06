package cardgamelibrary;

import com.google.gson.JsonObject;

import effects.EmptyEffect;
import game.Player;

public class SpellCard extends PlayableCard implements SpellInterface {

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

		System.out.println("ZONE THAT CARD PLAYED IS PASSED: " + z.toString());
		if (c.equals(this) && z == Zone.HAND) {
			System.out.println(
					"A spell's onCardPlayed has triggered for itself. Cost should be paid and it will go to " + "the graveyard.");
			System.out.println("SPELL NAME: " + getName());

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
