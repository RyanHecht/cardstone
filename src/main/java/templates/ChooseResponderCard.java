package templates;

import cardgamelibrary.Card;
import cardgamelibrary.Effect;
import cardgamelibrary.Zone;
import effects.EmptyEffect;

public interface ChooseResponderCard extends Card{

	@Override
	default public Effect onCardChosen(ChooseResponderCard chooser, Card chosen, Zone z) {
		if (this.equals(chooser)) {
			return getChooseEffect(chooser, chosen);
		}
		return EmptyEffect.create();
	}

	/**
	 * Gets some effect associated with the card the player has chosen.
	 *
	 * @param chooser
	 *          this card.
	 * @param chosenCard
	 *          the card the user chose.
	 * @return an effect describing how the game state should be changed based on
	 *         the user's choice of card.
	 */
	public default Effect getChooseEffect(ChooseResponderCard chooser, Card chosenCard) {
		return EmptyEffect.create();
	}
	
}
