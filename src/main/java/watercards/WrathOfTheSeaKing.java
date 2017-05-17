package watercards;

import cardgamelibrary.Board;
import cardgamelibrary.Card;
import cardgamelibrary.CardType;
import cardgamelibrary.ConcatEffect;
import cardgamelibrary.CreatureInterface;
import cardgamelibrary.Effect;
import cardgamelibrary.ManaPool;
import cardgamelibrary.SpellCard;
import cardgamelibrary.Zone;
import effects.ApplyEffect;
import effects.EffectMaker;
import game.Player;
import templates.decorators.CantAttackForTurnsCreature;

public class WrathOfTheSeaKing extends SpellCard{

	private static final String defaultImage = "images/WrathOfTheSeaKing.jpg";
	private static final String defaultName = "Wrath of the Sea King";
	private static final String defaultText = "Immobilize all enemy minions for 2 turns.";
	private static final CardType defaultType = CardType.SPELL;
	private int value;
	public WrathOfTheSeaKing(Player owner) {
		super(new ManaPool(50, 0, 3, 0, 0, 0), defaultImage, owner, defaultName, defaultText, defaultType);
		value = 0;
	}
	
	public Effect onThisPlayed(Card c, Zone z){
		return new EffectMaker((Board b)->{
			ConcatEffect ce = new ConcatEffect(this);
			for(Card card : b.getOcc(b.getOpposingPlayer(getOwner()), Zone.CREATURE_BOARD)){
				CantAttackForTurnsCreature caftc = new CantAttackForTurnsCreature((CreatureInterface)card,4);
				ce.addEffect(new ApplyEffect(card,caftc,this));
			}
			return ce;
		},this);
	}
	
}
