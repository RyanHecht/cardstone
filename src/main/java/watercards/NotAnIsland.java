package watercards;

import cardgamelibrary.CardType;
import cardgamelibrary.Creature;
import cardgamelibrary.CreatureInterface;
import cardgamelibrary.Effect;
import cardgamelibrary.ManaPool;
import cardgamelibrary.Zone;
import effects.EffectType;
import effects.EmptyEffect;
import effects.TransformEffect;
import game.Player;

public class NotAnIsland extends Creature{

	private static final int			defaultMaxHealth	= 2;
	private static final int			defaultAttack		= 1;
	private static final String		defaultImage			= "images/NotAnIsland.jpg";
	private static final String		defaultName			= "NotAnIsland";
	private static final String		defaultText			= "Whenver this would attack or be attacked, destory it and summon a leviathin";
	
	public NotAnIsland(Player owner) {
		super(defaultMaxHealth, defaultAttack, new ManaPool(50, 0, 2, 0, 0, 0), defaultImage, owner, defaultName, defaultText, CardType.CREATURE);
	}
	
	public Effect onCreatureAttack(CreatureInterface attacker, CreatureInterface target, Zone z){
		if(attacker.equals(this)){
			return new TransformEffect(this,new Leviathin(getOwner()),Zone.CREATURE_BOARD,this);
		}
		else if(target.equals(this)){
			return new TransformEffect(this,new Leviathin(getOwner()),Zone.CREATURE_BOARD,this);
		}
		return EmptyEffect.create();
	}
	
}
