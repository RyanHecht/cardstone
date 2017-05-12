package cardgamelibrary;

import com.google.gson.JsonObject;

import effects.AddToOccEffect;
import effects.EffectType;
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

		ConcatEffect effect = new ConcatEffect(this);
		if (c.equals(this) && z == Zone.HAND) {
			System.out.println(
					"A spell's onCardPlayed has triggered for itself. Cost should be paid and it will go to " + "the graveyard.");
			System.out.println("SPELL NAME: " + getName());

			// pay cost of the card.
			getOwner().payCost(getCost());

			effect.addEffect(new AddToOccEffect(this, getOwner(), Zone.GRAVE, Zone.HAND, this));
			// add any specific effects for this spell being played.
			effect.addEffect(onThisPlayed(c, z));
			effect.setType(EffectType.CARD_PLAYED);

		} else {
			effect.addEffect(this.onOtherCardPlayed(c, z));
		}
		return effect;
	}

}
