package cards;

import cardgamelibrary.Board;
import cardgamelibrary.Card;
import cardgamelibrary.CardType;
import cardgamelibrary.Creature;
import cardgamelibrary.Effect;
import cardgamelibrary.ManaPool;
import cardgamelibrary.SpellCard;
import cardgamelibrary.Zone;
import cards.templates.OnAnyAttackCard;
import effects.EmptyEffect;
import game.Player;

public class Undermine extends SpellCard implements OnAnyAttackCard{

	private static final ManaPool	defaultCost		= new ManaPool(20, 0, 0, 2, 0, 0);
	private static final String		defaultImage	= "images/Undermine.jpg";
	private static final String		defaultName		= "Undermine!";
	private static final String		defaultText		= "If an enemy minion attacks next turn, deal 4 to all minions.";
	private static final CardType	defaultType		= CardType.SPELL;
	private boolean isActive;
	private boolean shouldToggle;
	private int damage = 4;
	
	public Undermine(Player owner) {
		super(defaultCost, defaultImage, owner, defaultName, defaultText, defaultType);
		isActive = false;
		shouldToggle = false;
	}
	
	@Override
	public Effect onThisPlayed(Card c, Zone z){
		assert this == c;
		shouldToggle = true;
		return EmptyEffect.create();
	}
	
	public Effect onTurnStart(Player p, Zone z){
		if(shouldToggle){
			if(p != this.getOwner()){
				isActive = true;
			}
			else{
				isActive = false;
				shouldToggle = false;
			}
		}
		return EmptyEffect.create();
	}
	
	public Effect onAnyAttack(Creature attacker, Zone z){
		if(isActive){
			isActive = false;
			return (Board b) ->{
				for(Card c : b.getOcc(b.getActivePlayer(), Zone.CREATURE_BOARD)){
					b.damageCard((Creature)c, this, this.damage);
				}
				for(Card c : b.getOcc(b.getInactivePlayer(), Zone.CREATURE_BOARD)){
					b.damageCard((Creature)c, this, this.damage);
				}
			};
		}
		return EmptyEffect.create();
	}
}
