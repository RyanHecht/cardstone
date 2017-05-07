package cards;

import java.util.List;

import cardgamelibrary.Board;
import cardgamelibrary.Card;
import cardgamelibrary.CardType;
import cardgamelibrary.ConcatEffect;
import cardgamelibrary.Effect;
import cardgamelibrary.ManaPool;
import cardgamelibrary.SpellCard;
import cardgamelibrary.Zone;
import effects.AddToOccEffect;
import effects.GateEffect;
import game.Player;
import templates.PlayerChoosesCards;

public class DelveTheDepths extends SpellCard implements PlayerChoosesCards {

	private static final ManaPool	defaultCost		= new ManaPool(15, 0, 3, 0, 0, 0);
	private static final String		defaultImage	= "images/DelveTheDepths.jpg";
	private static final String		defaultName		= "Delve The Depths";
	private static final String		defaultText		= "Select any card from your deck and add it to your hand.";
	private static final CardType	defaultType		= CardType.SPELL;

	public DelveTheDepths(Player owner) {
		super(defaultCost, defaultImage, owner, defaultName, defaultText, defaultType);
		// TODO Auto-generated constructor stub
	}

	@Override
	public List<Card> getOptions(Board board) {
		// since this card searches your deck, the options list is your whole deck!
		return board.getOcc(getOwner(), Zone.DECK).getCards();
	}

	@Override
	public Effect getChooseEffect(PlayerChoosesCards thisCard, Card chosen) {
		ConcatEffect cE = new ConcatEffect(this);
		cE.addEffect(new GateEffect((Board board) ->{
			return board.getOcc(getOwner(),Zone.DECK).size() > 0;
		},this));
		cE.addEffect(new AddToOccEffect(chosen,getOwner(),Zone.HAND,Zone.DECK,this));
		return cE;
	}

	@Override
	public int getNumChoices() {
		return 1;
	}
}
