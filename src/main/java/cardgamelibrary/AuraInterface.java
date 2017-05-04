package cardgamelibrary;

public interface AuraInterface extends Card {
	@Override
	public Effect onCardPlayed(Card c, Zone z);
}
