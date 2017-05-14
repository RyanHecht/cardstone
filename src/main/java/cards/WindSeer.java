package cards;

import java.util.LinkedList;
import java.util.List;

import cardgamelibrary.Board;
import cardgamelibrary.Card;
import cardgamelibrary.CardType;
import cardgamelibrary.ConcatEffect;
import cardgamelibrary.Creature;
import cardgamelibrary.CreatureInterface;
import cardgamelibrary.Effect;
import cardgamelibrary.ElementType;
import cardgamelibrary.ManaPool;
import cardgamelibrary.SpellCard;
import cardgamelibrary.Zone;
import effects.ApplyEffect;
import effects.CardDrawEffect;
import effects.EffectMaker;
import effects.EmptyEffect;
import effects.SummonEffect;
import game.Player;
import templates.ChooseResponderCard;
import templates.PlayerChoosesCards;
import templates.decorators.HasteCreature;

public class WindSeer extends SpellCard implements PlayerChoosesCards{

	private static final String		defaultImage	= "images/WindSeer.jpg";
	private static final String		defaultName		= "Wind Seer";
	private static final String		defaultText		= "Predict whether your opponent will attack with a creature next turn for give all creatures in your hand haste.";
	private static final CardType	defaultType		= CardType.SPELL;
	private final SpellCard chooseYes; 
	private final SpellCard chooseNo; 
	int turnsLeft;
	int cardsPlayed;
	boolean should;
	private Card chosen;
	
	public WindSeer(Player owner) {
		super(new ManaPool(30, 0, 0, 0, 2, 0), defaultImage, owner, defaultName, defaultText, defaultType);
		chooseYes = new SpellCard(ManaPool.emptyPool(),"images/WindSeer.jpg",owner,"Predict Yes","Predict that your opponent will attack with a creature next turn.",CardType.SPELL);
		chooseNo = new SpellCard(ManaPool.emptyPool(),"images/WindSeerNo.jpg",owner,"Predict No","Predict that your opponent will not attack with a creature next turn.",CardType.SPELL);
		turnsLeft = 0;
		this.cardsPlayed = 0;
		should = false;
	}
	
	public Effect onThisPlayed(Card c, Zone z){
		turnsLeft = 2;
		return EmptyEffect.create();
	}
	
	@Override
	public List<Card> getOptions(Board board) {
		List<Card> res = new LinkedList<Card>();
		res.add(chooseYes);
		res.add(chooseNo);
		board.registerCard(chooseYes);
		board.registerCard(chooseNo);
		return res;
	}

	
	public Effect onCreatureAttack(CreatureInterface attacker, CreatureInterface target, Zone z) {
		if(!attacker.getOwner().equals(getOwner())){
			if(chosen.equals(chooseYes)){
				should = true;
			}
			else{
				should = false;
			}
		}
		return EmptyEffect.create();
	}

	public Effect onPlayerAttack(CreatureInterface attacker, Player target, Zone z) {
		if(!attacker.getOwner().equals(getOwner())){
			if(chosen.equals(chooseYes)){
				should = true;
			}
			else{
				should = false;
			}
		}
		return EmptyEffect.create();
	}
	
	public Effect onTurnStart(Player p, Zone z){
		if(turnsLeft == 1 && p == getOwner()  && should){
			turnsLeft = 0;
			return addHaste();
		}
		turnsLeft--;
		return EmptyEffect.create();
	}
	
	private Effect addHaste() {
		return new EffectMaker((Board b) -> {
			ConcatEffect ce = new ConcatEffect(this);
			for(Card c : b.getOcc(getOwner(), Zone.HAND)){
				if(c.isA(CreatureInterface.class)){
					HasteCreature hc = new HasteCreature((CreatureInterface) c);
					ce.addEffect(new ApplyEffect(c,hc,this));
				}
			}
			return ce;
		},this);
	}

	@Override
	public Effect getChooseEffect(ChooseResponderCard thisCard, Card chosen) {
		this.chosen = chosen;
		if(chosen == chooseNo){
			should = true;
		}
		return EmptyEffect.create();
	}

	@Override
	public int getNumChoices() {
		return 2;
	}
	
	
}
