package cards;

import cardgamelibrary.Board;
import cardgamelibrary.Card;
import cardgamelibrary.CardType;
import cardgamelibrary.Creature;
import cardgamelibrary.CreatureInterface;
import cardgamelibrary.Effect;
import cardgamelibrary.ElementType;
import cardgamelibrary.ManaPool;
import cardgamelibrary.SpellCard;
import cardgamelibrary.Zone;
import effects.AoeDamageEffect;
import effects.EmptyEffect;
import game.Player;
import lambda.FunctionThree;
import lambda.FunctionThreeMaker;

public class JaElilStormConduit extends Creature{

	private static final int			defaultMaxHealth	= 6;
	private static final int			defaultAttack		= 3;
	private static final String		defaultImage			= "images/JaElilStormConduit.jpg";
	private static final String		defaultName			= "Ja-Elil, Storm Conduit";
	private static final String		defaultText			= "Whenever you play an Air card, deal 1 to all characters.";
	
	public JaElilStormConduit(Player owner) {
		super(defaultMaxHealth, defaultAttack, new ManaPool(40, 0, 0, 0, 2, 0), defaultImage, owner, defaultName, defaultText, CardType.CREATURE);
	}
	
	public Effect onOtherCardPlayed(Card c, Zone z){
		if(c.getOwner().equals(getOwner()) && z.equals(Zone.CREATURE_BOARD)){
			if(c.hasElement(ElementType.AIR)){
				return new AoeDamageEffect(1, 1, 1, true, true,
			this, FunctionThreeMaker.targetsAllCreatures(),
			FunctionThreeMaker.determineCreatureDamage(0));
			}
		}
		return EmptyEffect.create();
	}
	
}
