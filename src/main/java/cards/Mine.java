package cards;

import cardgamelibrary.AuraCard;
import cardgamelibrary.Board;
import cardgamelibrary.Card;
import cardgamelibrary.CardType;
import cardgamelibrary.Effect;
import cardgamelibrary.ElementType;
import cardgamelibrary.ManaPool;
import cardgamelibrary.Zone;
import effects.EmptyEffect;
import game.Player;

public class Mine extends AuraCard{

	private static final ManaPool defaultCost = new ManaPool(10, 0, 0, 3, 0, 0);
	private static final String defaultImage = "images/Mine.jpg";
	private static final String defaultName = "Mine";
	private static final String defaultText = "At the start of your turns, gain 1 earth";
	private static final CardType defaultType = CardType.SPELL;

	public Mine(Player owner) {
		super(defaultCost, defaultImage, owner, defaultName, defaultText, defaultType);
	}
	
	public Effect onTurnStart(Player p, Zone z) {
		return (Board board) ->{
			if(z == Zone.AURA_BOARD){
				if(p == getOwner()){
					board.givePlayerElement(p, ElementType.EARTH, 1);
				}
			}
		};
	}
	
}
