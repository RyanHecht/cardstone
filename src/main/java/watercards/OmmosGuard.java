package watercards;

import cardgamelibrary.Board;
import cardgamelibrary.Card;
import cardgamelibrary.CardType;
import cardgamelibrary.Creature;
import cardgamelibrary.Effect;
import cardgamelibrary.ElementType;
import cardgamelibrary.ManaPool;
import cardgamelibrary.Zone;
import devotions.TideLevel;
import devotions.WaterDevotion;
import effects.EffectMaker;
import effects.EmptyEffect;
import game.Player;

public class OmmosGuard extends Creature{
	
	private static final int			defaultMaxHealth	= 5;
	private static final int			defaultAttack		= 4;
	private static final String		defaultImage			= "images/PoseidonsGuard.jpg";
	private static final String		defaultName			= "Ommos' Guard";
	private static final String		defaultText			= "Low tide: destroy 6 enemy element that aren't water";
	
	public OmmosGuard(Player owner) {
		super(defaultMaxHealth, defaultAttack, new ManaPool(50, 0, 2, 0, 0, 0), defaultImage, owner, defaultName, defaultText, CardType.CREATURE);
	}
	
	public Effect onThisPlayed(Card c, Zone z){
		if(WaterDevotion.getLevelOfWater(getOwner().getDevotion()).equals(TideLevel.LOW)){
			return new EffectMaker((Board b)->{
				int removed = 0;
				Player opponent = b.getOpposingPlayer(getOwner());
				for(ElementType et : ElementType.values()){
					if(!et.equals(ElementType.WATER)){
						while(removed < 6 && opponent.getElem(et) > 0){
							removed++;
							opponent.setElement(et, opponent.getElem(et) - 1);
						}
					}
				}
				return EmptyEffect.create();
			},this);
		}
		return EmptyEffect.create();
	}
	
}
