package cards;

import cardgamelibrary.Board;
import cardgamelibrary.Card;
import cardgamelibrary.CardType;
import cardgamelibrary.ConcatEffect;
import cardgamelibrary.Creature;
import cardgamelibrary.CreatureInterface;
import cardgamelibrary.Effect;
import cardgamelibrary.ManaPool;
import cardgamelibrary.Zone;
import effects.ApplyEffect;
import effects.EffectMaker;
import game.Player;
import templates.ActiveOneTurnCard;
import templates.decorators.WindfuryWrapper;

public class LadyOfTheMists extends Creature implements ActiveOneTurnCard{

	private static final String defaultImage = "images/LadyOfTheMists.jpg";
	private static final String defaultName = "Lady of the Mists";
	private static final String defaultText = "In 2 turns, give all your currently living creature windfury.";
	private static final CardType defaultType = CardType.CREATURE;
	private static int defaultMaxHealth = 4;
	private static int defaultAttack = 5;
	int turnsLeft = 0;
	
	public LadyOfTheMists(Player owner) {
		super(defaultMaxHealth, defaultAttack, new ManaPool(50, 0, 0, 0, 1, 0), defaultImage, owner, defaultName, defaultText, CardType.CREATURE);
	}
	
	

	@Override
	public void decrementValue() {
		turnsLeft--;
	}

	@Override
	public int getValue() {
		return turnsLeft;
	}

	@Override
	public void setValue(int value) {
		turnsLeft = value;
	}

	@Override
	public int getInitValue() {
		return 5;
	}
	
	public Effect activate(){
		return new EffectMaker((Board b) -> {
			ConcatEffect ce = new ConcatEffect(this);
			for(Card c : b.getOcc(getOwner(), Zone.CREATURE_BOARD)){
				WindfuryWrapper ww = new WindfuryWrapper((CreatureInterface) c);
				ce.addEffect(new ApplyEffect(c,ww,this));
			}
			return ce;
		},this);
	}
	
}
