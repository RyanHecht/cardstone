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
import effects.CardDrawEffect;
import effects.EmptyEffect;
import effects.SummonEffect;
import game.Player;
import templates.ChooseResponderCard;
import templates.PlayerChoosesCards;

public class StormWatcher extends SpellCard implements PlayerChoosesCards{

	private static final String		defaultImage	= "images/StormWatcher.jpg";
	private static final String		defaultName		= "Storm Watcher";
	private static final String		defaultText		= "Draw a card. Predict whether your opponent will play at least 3 cards next turn for draw 2 cards.";
	private static final CardType	defaultType		= CardType.SPELL;
	private final SpellCard chooseYes; 
	private final SpellCard chooseNo; 
	int turnsLeft;
	int cardsPlayed;
	boolean should;
	private Card chosen;
	private Card last;
	
	public StormWatcher(Player owner) {
		super(new ManaPool(20, 0, 0, 0, 1, 0), defaultImage, owner, defaultName, defaultText, defaultType);
		chooseYes = new SpellCard(ManaPool.emptyPool(),"images/StormWatcher.jpg",owner,"Predict Yes","Predict that your opponent will play at least 3 cards next turn.",CardType.SPELL);
		chooseNo = new SpellCard(ManaPool.emptyPool(),"images/StormWatcherNo.jpg",owner,"Predict No","Predict that your opponent will not play at least 3 cards next turn.",CardType.SPELL);
		turnsLeft = 0;
		this.last = chooseYes;
		this.cardsPlayed = 0;
	}
	
	public Effect onThisPlayed(Card c, Zone z){
		turnsLeft = 2;
		cardsPlayed = 0;
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
		if(turnsLeft == 1){
			cardsPlayed++;
		}
		return EmptyEffect.create();
	}
	

	
	public Effect onTurnStart(Player p, Zone z){
		if(turnsLeft == 1 && p == getOwner()){
			turnsLeft = 0;
			if(this.chosen.equals(this.chooseYes)){
				if(cardsPlayed >= 3){
					return drawTwo();
				}
				return EmptyEffect.create();
			}
			else{
				if(cardsPlayed < 3){
					return drawTwo();
				}
				return EmptyEffect.create();
			}
		}
		turnsLeft--;
		return EmptyEffect.create();
	}
	
	private Effect drawTwo() {
		ConcatEffect ce = new ConcatEffect(this);
		ce.addEffect(new CardDrawEffect(getOwner(),this));
		ce.addEffect(new CardDrawEffect(getOwner(),this));
		return ce;
	}

	@Override
	public Effect getChooseEffect(ChooseResponderCard thisCard, Card chosen) {
		this.chosen = chosen;
		return EmptyEffect.create();
	}

	@Override
	public int getNumChoices() {
		return 2;
	}
	
}
