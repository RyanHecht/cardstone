package cards;

import java.util.LinkedList;
import java.util.List;

import cardgamelibrary.Board;
import cardgamelibrary.Card;
import cardgamelibrary.CardType;
import cardgamelibrary.ConcatEffect;
import cardgamelibrary.Effect;
import cardgamelibrary.ElementType;
import cardgamelibrary.ManaPool;
import cardgamelibrary.SpellCard;
import cardgamelibrary.Zone;
import effects.AddToOccEffect;
import effects.EmptyEffect;
import effects.GateEffect;
import effects.SummonEffect;
import game.Player;
import templates.PlayerChoosesCards;

public class BoltCatcher extends SpellCard implements PlayerChoosesCards{

	private static final String		defaultImage	= "images/BoltCatcher.jpg";
	private static final String		defaultName		= "Bolt Catcher";
	private static final String		defaultText		= "Predict whether your opponent will play a spell next turn. If you are right, gain a copy of the last spell they played, which costs nothing.";
	private static final CardType	defaultType		= CardType.SPELL;
	private final SpellCard chooseYes; 
	private final SpellCard chooseNo; 
	int turnsLeft;
	boolean should;
	private Card chosen;
	private Card last;
	
	public BoltCatcher(Player owner) {
		super(new ManaPool(10, 0, 0, 0, 1, 0), defaultImage, owner, defaultName, defaultText, defaultType);
		chooseYes = new SpellCard(ManaPool.emptyPool(),"images/BoltCatcher.jpg",owner,"Predict Yes","Predict that your opponent will play a spell during their next turn.",CardType.SPELL);
		chooseNo = new SpellCard(ManaPool.emptyPool(),"images/BoltCatcherNo.jpg",owner,"Predict No","Predict that your opponent will not play a spell during their next turn.",CardType.SPELL);
		turnsLeft = 0;
		should = false;
		this.last = chooseYes;
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

	public Effect onOtherCardPlayed(Card c, Zone z){
		if(c.getType() == CardType.SPELL && c.getOwner() != getOwner()){
			this.last = c;
		}
		if(turnsLeft == 1){
			if(c.getType() == CardType.SPELL){
				if(this.chosen.equals(chooseNo)){
						should = false;
				}
				else{
					return getCard();
				}
			}
		}
		return EmptyEffect.create();
	}
	
	Effect getCard(){
		turnsLeft = 0;
		Card newCard = last.getNewInstanceOf(getOwner());
		ManaPool mp = newCard.getCost();
		mp.setResources(0);
		for(ElementType type : ElementType.values()){
			mp.setElement(type, 0);
		}
		return new SummonEffect(newCard,Zone.HAND,this);
	}
	
	public Effect onTurnStart(Player p, Zone z){
		if(turnsLeft == 1 && p == getOwner() && z == Zone.GRAVE && should){
			turnsLeft = 0;
			return getCard();
		}
		turnsLeft--;
		return EmptyEffect.create();
	}
	
	@Override
	public Effect getChooseEffect(PlayerChoosesCards thisCard, Card chosen) {
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
