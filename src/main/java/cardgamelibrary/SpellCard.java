package cardgamelibrary;

import game.Player;

public class SpellCard extends PlayableCard {

	public SpellCard(ManaPool cost, String image, Player owner, String name, String text, CardType type) {
		super(cost, image, owner, name, text, type);
	}

}
