package cardgamelibrary;

public interface SpellInterface extends Card {

	@Override
	public Effect onCardPlayed(Card c, Zone z);
}
