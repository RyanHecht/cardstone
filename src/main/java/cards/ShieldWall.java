package cards;

import cardgamelibrary.AuraCard;
import cardgamelibrary.Board;
import cardgamelibrary.Card;
import cardgamelibrary.CardType;
import cardgamelibrary.ConcatEffect;
import cardgamelibrary.CreatureInterface;
import cardgamelibrary.Effect;
import cardgamelibrary.ManaPool;
import cardgamelibrary.OrderedCardCollection;
import cardgamelibrary.Zone;
import devotions.EarthDevotion;
import effects.AddToOccEffect;
import effects.ApplyEffect;
import effects.EffectMaker;
import effects.EmptyEffect;
import game.Player;
import templates.decorators.Buffer;
import templates.decorators.CreatureWrapper;
import templates.decorators.TauntCreature;

public class ShieldWall extends AuraCard implements Buffer{

	private static final String defaultImage = "images/ShieldWall.jpg";
	private static final String defaultName = "Shield Wall";
	private static final String defaultText = "Aura. Your minions have taunt and +(sleeping stone)/+(sleeping stone). Destroy this after any of your minions attacks.";
	private static final CardType defaultType = CardType.AURA;

	public ShieldWall(Player owner) {
		super(new ManaPool(40, 0, 0, 2, 0, 0), defaultImage, owner, defaultName, defaultText, defaultType);
	}

	public Effect onThisPlayed(Card c, Zone z){
		return new EffectMaker((Board b) -> {
			ConcatEffect ce = new ConcatEffect(this);
			OrderedCardCollection myBoard = b.getOcc(getOwner(), Zone.CREATURE_BOARD);
			for(Card card : myBoard){
				ShieldWallWrapper sww = new ShieldWallWrapper((CreatureInterface)card,this);
				ce.addEffect(new ApplyEffect(card,sww,this));
			}
			return ce;
		},this);
	}
	
	public Effect onZoneChange(Card c, OrderedCardCollection dest, OrderedCardCollection start, Zone z) {
		if(c.getType().equals(CardType.CREATURE)){
			if(dest.getZone().equals(Zone.CREATURE_BOARD)){
				if(c.getOwner().equals(getOwner())){
					ShieldWallWrapper sww = new ShieldWallWrapper((CreatureInterface)c,this);
					return new ApplyEffect(c,sww,this);
				}
			}
		}
		return EmptyEffect.create();
	}
	
	public Effect onCreatureAttack(CreatureInterface attacker, CreatureInterface target, Zone z){
		if(z.equals(Zone.AURA_BOARD) && attacker.getOwner().equals(getOwner())){
			return new AddToOccEffect(this, getOwner(), Zone.GRAVE, Zone.AURA_BOARD, this);
		}
		return EmptyEffect.create();
	}
	public Effect onCardZoneCreated(Card card, Zone location, Zone zone) {
		if(card.getType().equals(CardType.CREATURE)){
			if(location.equals(Zone.CREATURE_BOARD)){
				if(card.getOwner().equals(getOwner())){
					ShieldWallWrapper sww = new ShieldWallWrapper((CreatureInterface)card,this);
					return new ApplyEffect(card,sww,this);
				}
			}
		}
		return EmptyEffect.create();
	}

	
	@Override
	public int getBuff(CreatureInterface c) {
		return EarthDevotion.getLevelOfEarth(getOwner().getDevotion());
	}
	
	private static class ShieldWallWrapper extends TauntCreature{
		private ShieldWall owner;
		public ShieldWallWrapper(CreatureInterface c, ShieldWall owner) {
			super(c);
			curHealthBuff = owner.getBuff(c);
			curAttackBuff = owner.getBuff(c);
			this.owner = owner;
		}
		int curHealthBuff;
		int curAttackBuff;
		public int getAttack(){
			if(active){
				curAttackBuff = owner.getBuff(this);
				return curAttackBuff + creatureInternal.getAttack();
			}
			return creatureInternal.getAttack();
		}
		
		public int getHealth(){
			if(active){
				curHealthBuff = owner.getBuff(this);
				return curHealthBuff + creatureInternal.getHealth();
			}
			return creatureInternal.getAttack();
		}
		
		public Effect onZoneChange(Card c, OrderedCardCollection dest, OrderedCardCollection start, Zone z) {
			if(c.equals(owner) && dest.getZone() != Zone.AURA_BOARD){
				this.deactivate();
			}
			if(this.equals(c)){
				this.deactivate();
			}
			return EmptyEffect.create();
		}
		
	}
	
	
	
}
