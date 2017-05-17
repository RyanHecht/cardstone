package watercards;

import cardgamelibrary.Board;
import cardgamelibrary.Card;
import cardgamelibrary.CardType;
import cardgamelibrary.ConcatEffect;
import cardgamelibrary.Effect;
import cardgamelibrary.ManaPool;
import cardgamelibrary.OrderedCardCollection;
import cardgamelibrary.SpellCard;
import cardgamelibrary.Zone;
import effects.AddToOccCrossPlayerEffect;
import effects.EffectMaker;
import effects.EmptyEffect;
import game.Player;
import templates.TargetsOtherCard;
import templates.TargetsPlayer;
import templates.decorators.CantAttackForTurnsCreature;

public class CallOfTheSeaKing extends SpellCard {

	private static final String defaultImage = "images/CallOfTheSeaKing.jpg";
	private static final String defaultName = "Call of the Sea King";
	private static final String defaultText = "Steal a random enemy minion.";
	private static final CardType defaultType = CardType.SPELL;
	private int value;
	public CallOfTheSeaKing(Player owner) {
		super(new ManaPool(50, 0, 3, 0, 0, 0), defaultImage, owner, defaultName, defaultText, defaultType);
		value = 0;
	}
	
	public Effect onThisPlayed(Card c, Zone z){
		return new EffectMaker((Board board) -> {
			Player opp = board.getOpposingPlayer(getOwner());
			OrderedCardCollection occ = board.getOcc(opp, Zone.CREATURE_BOARD);
			if(occ.size() > 0){
				return new AddToOccCrossPlayerEffect(occ.getFirstCard(),
						opp,getOwner(),Zone.CREATURE_BOARD,Zone.CREATURE_BOARD,this);
			}
			return EmptyEffect.create();
			
		},this);
	}

	
	
}
