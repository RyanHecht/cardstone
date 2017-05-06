package cards;

import cardgamelibrary.AuraCard;
import cardgamelibrary.Board;
import cardgamelibrary.Card;
import cardgamelibrary.CardType;
import cardgamelibrary.ConcatEffect;
import cardgamelibrary.Effect;
import cardgamelibrary.ElementType;
import cardgamelibrary.ManaPool;
import cardgamelibrary.Zone;
import effects.GateEffect;
import effects.PlayerHealedEffect;
import game.Player;

public class ForestOfLife extends AuraCard{

	
	private static final ManaPool defaultCost = new ManaPool(10, 0, 0, 1, 0, 0);
	private static final String defaultImage = "images/ForestOfLife.jpg";
	private static final String defaultName = "Forest Of Life";
	private static final String defaultText = "Whenever you play a card with earth in its cost, gain 3 life.";
	private static final CardType defaultType = CardType.AURA;

	public ForestOfLife(Player owner) {
		super(defaultCost, defaultImage, owner, defaultName, defaultText, defaultType);
	}
	
	public Effect onOtherCardPlayed(Card c, Zone z){
		ConcatEffect ce = new ConcatEffect();
		ce.addEffect(new GateEffect((Board board) -> {
			return z == Zone.AURA_BOARD && c.getOwner() 
					== getOwner() && c.getCost().getElement(ElementType.EARTH) >= 1;
		}));
		ce.addEffect(new PlayerHealedEffect(this,getOwner(),3));
		return ce;
	}
}
