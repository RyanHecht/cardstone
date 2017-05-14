package cards;

import java.util.LinkedList;
import java.util.List;

import cardgamelibrary.Board;
import cardgamelibrary.Card;
import cardgamelibrary.CardType;
import cardgamelibrary.ConcatEffect;
import cardgamelibrary.Creature;
import cardgamelibrary.Effect;
import cardgamelibrary.ManaPool;
import cardgamelibrary.Zone;
import effects.AddToOccEffect;
import effects.EffectMaker;
import game.Player;

public class SkyCavalry extends Creature{

	private static final String defaultImage = "images/SkyCavalry.jpg";
	private static final String defaultName = "Sky Cavalry";
	private static final String defaultText = "Haste. On play, summon up to (storm count / 2) other cards from you deck and hand named Sky Cavalry onto the board.";
	private static final CardType defaultType = CardType.CREATURE;
	private static int defaultMaxHealth = 1;
	private static int defaultAttack = 2;

	public SkyCavalry(Player owner) {
		super(defaultMaxHealth, defaultAttack, new ManaPool(65, 0, 0, 0, 4, 0), defaultImage, owner, defaultName, defaultText, CardType.CREATURE);
	}

	public Effect onThisPlayed(Card c, Zone z){
		EffectMaker em = new EffectMaker((Board b) -> {
			ConcatEffect ce = new ConcatEffect(this);
			List<SkyCavalry> cavalry = new LinkedList<>();
			for(Card card : b.getOcc(getOwner(), Zone.DECK)){
				if(card.isA(SkyCavalry.class)){
					ce.addEffect(getSummonSkyCavalryEffect((SkyCavalry) c,Zone.DECK));
				}
			}
			for(Card card : b.getOcc(getOwner(), Zone.HAND)){
				if(card.isA(SkyCavalry.class)){
					ce.addEffect(getSummonSkyCavalryEffect((SkyCavalry) c,Zone.HAND));
				}
			}
			return ce;
		},this);
		setHaste();
		return em;
	}

	public void setHaste(){
		this.allowAttack();
	}

	
	private Effect getSummonSkyCavalryEffect(SkyCavalry c, Zone start) {
		c.setHaste();
		return new AddToOccEffect(c,getOwner(),Zone.CREATURE_BOARD,start,this);
	}
	
}
