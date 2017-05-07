package cards;

import cardgamelibrary.AuraCard;
import cardgamelibrary.Board;
import cardgamelibrary.Card;
import cardgamelibrary.CardType;
import cardgamelibrary.ConcatEffect;
import cardgamelibrary.CreatureInterface;
import cardgamelibrary.Effect;
import cardgamelibrary.ManaPool;
import cardgamelibrary.Zone;
import effects.CardDamageEffect;
import effects.EffectMaker;
import effects.EmptyEffect;
import game.Player;

public class IntermittentTremors extends AuraCard{

	private static final ManaPool defaultCost = new ManaPool(15, 0, 0, 2, 0, 0);
	private static final String defaultImage = "images/IntermittentTremors.jpg";
	private static final String defaultName = "Intermittent Tremors";
	private static final String defaultText = "At the start of every other one of your turns, deal 1 to all minions";
	private static final CardType defaultType = CardType.AURA;
	private int turnsToTremor;

	public IntermittentTremors(Player owner) {
		super(defaultCost, defaultImage, owner, defaultName, defaultText, defaultType);
		turnsToTremor = 0;
	}
	
	public Effect onThisPlayed(Card c, Zone z){
		turnsToTremor = 1;
		return EmptyEffect.create();
	}
	
	public Effect onTurnStart(Player p, Zone z) {
		if(z == Zone.AURA_BOARD){
			if(turnsToTremor == 0){
				turnsToTremor = 3;
				return new EffectMaker((Board board) -> {
					ConcatEffect ce = new ConcatEffect(this);
					for (Card c : board.getPlayerOneCreatures()) {
						CreatureInterface cr = (CreatureInterface) c;
						ce.addEffect(new CardDamageEffect(this, cr, 1));
					}
					for (Card c : board.getPlayerTwoCreatures()) {
						CreatureInterface cr = (CreatureInterface) c;
						ce.addEffect(new CardDamageEffect(this, cr, 1));
					}
					return ce;
				},this);
			}
			else{
				turnsToTremor--;
			}
		}
		return EmptyEffect.create();
	}
	
}
