package effects;

import java.lang.reflect.InvocationTargetException;

import cardgamelibrary.Board;
import cardgamelibrary.Card;
import cardgamelibrary.Effect;
import cardgamelibrary.Zone;
import game.Player;

public class CastEffect implements Effect{

	
	
	private Card src;
	private Class toCast;
	private Player owner;

	
	public CastEffect(Card src, Class toCast, Player owner) {
		super();
		this.src = src;
		this.toCast = toCast;
		this.owner = owner;
	}

	public Class<Card> getToCast() {
		return toCast;
	}

	public void setToCast(Class toCast) {
		this.toCast = toCast;
	}

	public Player getOwner() {
		return owner;
	}

	public void setOwner(Player owner) {
		this.owner = owner;
	}

	public void setSrc(Card src) {
		this.src = src;
	}

	@Override
	public void apply(Board board) {
		try {
			Card c = (Card) toCast.getConstructor(Player.class).newInstance(owner);
			c.onThisPlayed(c, Zone.GRAVE).apply(board);
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public EffectType getType() {
		return EffectType.CAST_EFFECT;
	}

	@Override
	public Card getSrc() {
		return src;
	}

	
	
}
