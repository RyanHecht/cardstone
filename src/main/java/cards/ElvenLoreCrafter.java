package cards;

import cardgamelibrary.Allegiance;
import cardgamelibrary.CardType;
import cardgamelibrary.Creature;
import cardgamelibrary.Effect;
import cardgamelibrary.ManaPool;
import cardgamelibrary.Zone;
import effects.CardDrawEffect;
import effects.EmptyEffect;
import game.Player;

public class ElvenLoreCrafter extends Creature{

	private static final int			defaultMaxHealth	= 6;
	private static final int			defaultAttack		= 1;
	private static final String		defaultImage			= "images/ElvenLoreCrafter.jpg";
	private static final String		defaultName			= "Elven Lore Crafter";
	private static final String		defaultText			= "Elf. If, on any turn where this could attack, it does not, draw a card.";
	
	public ElvenLoreCrafter(Player owner) {
		super(defaultMaxHealth, defaultAttack, new ManaPool(25, 0, 0, 0, 2, 0), defaultImage, owner, defaultName, defaultText, CardType.CREATURE);
		allegianceTypes.add(Allegiance.ELF);
	}
	
	
	
	public Effect onTurnEnd(Player p, Zone z){
		if(getNumAttacks() > 0){
			return new CardDrawEffect(p,this);
		}
		return EmptyEffect.create();
	}
	
}
