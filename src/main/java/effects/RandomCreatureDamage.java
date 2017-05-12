package effects;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import cardgamelibrary.Board;
import cardgamelibrary.Card;
import cardgamelibrary.CardType;
import cardgamelibrary.CreatureInterface;
import cardgamelibrary.Zone;

public class RandomCreatureDamage implements DamageInterface{

	private int dmg;
	private Card src;
	private boolean ownCards;
	private boolean enemyCards;
	private static Random rand = new Random();
	
	
	
	public RandomCreatureDamage( boolean ownCards, boolean enemyCards,int dmg, Card src) {
		super();
		this.dmg = dmg;
		this.src = src;
		this.ownCards = ownCards;
		this.enemyCards = enemyCards;
	}

	@Override
	public void apply(Board board) {
		List<Card> c = new ArrayList<Card>();
		if(ownCards){
			c.addAll(board.getOcc(src.getOwner(), Zone.CREATURE_BOARD));
		}
		if(enemyCards){
			c.addAll(board.getOcc(board.getOpposingPlayer(src.getOwner()), Zone.CREATURE_BOARD));
		}
		if(c.size() == 0){
			return;
		}
		Card target = c.get(rand.nextInt(c.size()));
		if(target.getType() != CardType.CREATURE){
			return;
		}
		board.damageCard((CreatureInterface) target, getSrc(), dmg);
	}

	@Override
	public EffectType getType() {
		return EffectType.CARD_DAMAGED;
	}

	@Override
	public Card getSrc() {
		return src;
	}

	@Override
	public void setDamage(int dmg) {
		this.dmg = dmg;
	}

	@Override
	public int getDamage() {
		return dmg;
	}

}
