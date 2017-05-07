package cardgamelibrary;

import com.google.gson.JsonObject;

import effects.AddToOccEffect;
import effects.EmptyEffect;
import game.Player;

public class AuraCard extends PlayableCard implements AuraInterface {

	public AuraCard(ManaPool cost, String image, Player owner, String name, String text, CardType type) {
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
			// pay cost of the card.
			getOwner().payCost(getCost());
			effect.addEffect(new AddToOccEffect(this, getOwner(), Zone.AURA_BOARD, Zone.HAND,this));

			// add any specific effects for this aura being played.
			effect.addEffect(onThisPlayed(c, z));
		}
		else{
			effect.addEffect(this.onOtherCardPlayed(c, z));
		}
		return effect;
	}
}
