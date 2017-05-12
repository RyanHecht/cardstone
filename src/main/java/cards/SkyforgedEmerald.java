package cards;

import cardgamelibrary.Card;
import cardgamelibrary.CardType;
import cardgamelibrary.DevotionType;
import cardgamelibrary.Effect;
import cardgamelibrary.ElementType;
import cardgamelibrary.ManaPool;
import cardgamelibrary.SpellCard;
import cardgamelibrary.Zone;
import effects.EmptyEffect;
import effects.GiveElementEffect;
import game.Player;

public class SkyforgedEmerald extends SpellCard{

	private static final String defaultImage = "images/SkyforgedEmerald.png";
	private static final String defaultName = "Skyforged Emerald";
	private static final String defaultText = "If you are devoted to air, gain 6 air next turn.";
	private static final CardType defaultType = CardType.SPELL;
	private boolean active;

	public SkyforgedEmerald(Player owner) {
		super(new ManaPool(0, 0, 0, 0, 1, 0), defaultImage, owner, defaultName, defaultText, defaultType);
		active = false;
	}
	
	public Effect onThisPlayed(Card c){
		if(getOwner().getDevotion().getDevotionType().equals(DevotionType.AIR)){
			active = true;
		}
		return EmptyEffect.create();
	}
	
	
	public Effect onTurnStart(Player p, Zone z){
		if(active){
			
			if(p.equals(getOwner())){
				active = false;
				return new GiveElementEffect(getOwner(),ElementType.AIR,6,this);
			}
		}
		return EmptyEffect.create();
	}
}
