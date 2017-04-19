package cards.spells;

import cardgamelibrary.Board;
import cardgamelibrary.Card;
import cardgamelibrary.CardType;
import cardgamelibrary.Creature;
import cardgamelibrary.Effect;
import cardgamelibrary.ManaPool;
import cardgamelibrary.SpellCard;
import cardgamelibrary.Zone;
import effects.EmptyEffect;
import game.Player;

public class SandTomb extends SpellCard{

	public SandTomb( Player owner) {
		super(defaultCost, defaultImage, owner, defaultName, defaultText, defaultType);
		
	}

	private static final ManaPool defaultCost = new ManaPool(25,0,0,1,0,0);
	private static final String defaultImage = "images/sandTomb.jpg";
	private static final String defaultName = "Sand Tomb";
	private static final String defaultText = "Imprison an enemy minion in a 0/8 Tomb with taunt on your side of the board. When the Tomb is destroyed, return the minion.";
	private static final CardType defaultType = CardType.SPELL;
	
	public boolean isValidTarget(Card card){
		if(card.getType() != CardType.CREATURE){
			return false;
		}
		if(this.getOwner() != card.getOwner()){
			return false;
		}
		return true;
	}
	
	public Effect onTargetedPlay(Card target){
		assert isValidTarget(target);
		Creature tomb = new Tomb(target,getOwner());
		return (Board board) -> {
			board.transformCard(target, tomb, Zone.BOARD);
		};
	}
	
	private static class Tomb extends Creature{
		private static final ManaPool defaultCost = new ManaPool(0,0,0,1,0,0);
		private static final String defaultImage = "images/Tomb.jpg";
		private static final String defaultName = "Tomb";
		private static final String defaultText = "Destroy this to free the imprisoned minion!";
		private static final int defaultHealth = 8;
		private static final int defaultAttack = 0;
		private static final CardType defaultType = CardType.CREATURE;
		Card resummon;
		private Tomb(Card resummon, Player owner){
			super(defaultAttack,defaultHealth,defaultCost, defaultImage, owner, defaultName,
					defaultText, defaultType);
			this.resummon = resummon;
		}
		@Override
		public Effect onCreatureDeath(Creature cr, Zone z) {
			if(cr != this){
				return EmptyEffect.create();
			}
			else{
				return(Board board) ->{
					board.summonCard(resummon,Zone.CREATURE_BOARD);
				};
			}
		}
		
		
	}
	
}
