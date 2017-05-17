package watercards;

import cardgamelibrary.Board;
import cardgamelibrary.Card;
import cardgamelibrary.CardType;
import cardgamelibrary.ConcatEffect;
import cardgamelibrary.Creature;
import cardgamelibrary.CreatureInterface;
import cardgamelibrary.Effect;
import cardgamelibrary.Event;
import cardgamelibrary.ManaPool;
import cardgamelibrary.SpellCard;
import cardgamelibrary.Zone;
import devotions.AirDevotion;
import devotions.TideLevel;
import devotions.WaterDevotion;
import effects.ApplyEffect;
import effects.EffectMaker;
import effects.GiveResourceEffect;
import effects.KillCreatureEffect;
import game.Player;
import templates.ActiveOneTurnCard;
import templates.decorators.CantAttackForTurnsCreature;


public class TheBigOne extends SpellCard implements ActiveOneTurnCard{

	private static final String defaultImage = "images/TheBigOne.jpg";
	private static final String defaultName = "The Big One";
	private static final String defaultText = "Immobilize the opponents board for two turns. High tide: instead destroy the opponents board and immobilize their hand for 1 turn.";
	private static final CardType defaultType = CardType.SPELL;
	private int value;
	public TheBigOne(Player owner) {
		super(new ManaPool(150, 0, 5, 0, 0, 0), defaultImage, owner, defaultName, defaultText, defaultType);
		value = 0;
	}
	
	public boolean onProposedLegalityEvent(Event e, Zone z){
		if(isActive()){
			return true;
		}
		return false;
	}
	
	public String getComplaint(Event e, Zone z){
		return "Cannot play cards this turn because of the big one";
	}
	
	public Effect onThisPlayed(Card c, Zone z){
		if(WaterDevotion.getLevelOfWater(getOwner().getDevotion()).equals(TideLevel.HIGH)){
			value = 2;
			return new EffectMaker((Board b) -> {
				ConcatEffect ce = new ConcatEffect(this);
				for(Card card : b.getOcc(b.getOpposingPlayer(getOwner()),Zone.CREATURE_BOARD)){
					ce.addEffect(new KillCreatureEffect(this,(CreatureInterface)card));
				}
				return ce;
			},this);
		}
		else{
			return new EffectMaker((Board b) -> {
				ConcatEffect ce = new ConcatEffect(this);
				for(Card card : b.getOcc(b.getOpposingPlayer(getOwner()), Zone.CREATURE_BOARD)){
					CantAttackForTurnsCreature catc = new CantAttackForTurnsCreature((CreatureInterface)card,5);
					ce.addEffect(new ApplyEffect(card,catc,this));
				}
				return ce;
			},this);
		}
	}

	@Override
	public void decrementValue() {
		value--;
	}

	@Override
	public int getValue() {
		return value;
	}

	@Override
	public void setValue(int value) {
		value = value;
	}

	@Override
	public int getInitValue() {
		return 2;
	}
	
}
