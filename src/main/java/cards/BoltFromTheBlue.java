package cards;

import cardgamelibrary.Card;
import cardgamelibrary.CardType;
import cardgamelibrary.ConcatEffect;
import cardgamelibrary.CreatureInterface;
import cardgamelibrary.Effect;
import cardgamelibrary.ElementType;
import cardgamelibrary.ManaPool;
import cardgamelibrary.SpellCard;
import cardgamelibrary.Zone;
import effects.CardDamageEffect;
import effects.EmptyEffect;
import effects.PlayerDamageEffect;
import effects.SummonEffect;
import game.Player;
import templates.TargetsOtherCard;
import templates.TargetsPlayer;

public class BoltFromTheBlue extends SpellCard implements TargetsOtherCard, TargetsPlayer {

	private static final String		defaultImage	= "images/BoltFromTheBlue.jpg";
	private static final String		defaultName		= "Bolt From The Blue";
	private static final String		defaultText		= "Deal 3. If this is the first storm card you played this game, deal 5 instead.";
	private static final CardType	defaultType		= CardType.SPELL;
	private boolean deal5;
	
	public BoltFromTheBlue(Player owner) {
		super(new ManaPool(15, 0, 0, 0, 1, 0), defaultImage, owner, defaultName, defaultText, defaultType);
		deal5 = true;
	}
	
	public Effect onOtherCardPlayed(Card c, Zone z){
		if(c.getOwner() == getOwner() && c.getCost().getElement(ElementType.AIR) > 0){
			deal5 = false;
		}
		return EmptyEffect.create();
	}
	
	@Override
	public boolean playerValidTarget(Player p) {
		if (p.equals(getOwner())) {
			// can't cast on self.
			return false;
		}
		return true;
	}

	@Override
	public boolean cardValidTarget(Card card, Zone targetIn) {
		if (card.getOwner().equals(getOwner())) {
			// can't cast on own creatures.
			return false;
		}
		if (card.isA(CreatureInterface.class) && targetIn == Zone.CREATURE_BOARD) {
			// card must be a creature and on board.
			return true;
		}
		return false;
	}

	@Override
	public Effect impactCardTarget(Card target, Zone targetIn) {
		if(deal5){
			return new CardDamageEffect(this, (CreatureInterface) target, 5);
		}
		return new CardDamageEffect(this, (CreatureInterface) target, 3);
	}
	@Override
	public Effect impactPlayerTarget(Player p) {
		if(deal5){
			return new PlayerDamageEffect(p, this, 5);
		}
		return new PlayerDamageEffect(p, this, 3);
	}

	
}
