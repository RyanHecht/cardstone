package cards;

import cardgamelibrary.Card;
import cardgamelibrary.CardType;
import cardgamelibrary.Creature;
import cardgamelibrary.Effect;
import cardgamelibrary.ManaPool;
import cardgamelibrary.Zone;
import devotions.AirDevotion;
import effects.CardDamageEffect;
import effects.CreatureAttackChangeEffect;
import effects.EmptyEffect;
import effects.PlayerDamageEffect;
import game.Player;
import templates.ActivateTargetingCard;
import templates.ActivateTargetingPlayer;

public class AlJunLightningKing extends Creature implements ActivateTargetingCard, ActivateTargetingPlayer{

	private static final String		defaultImage	= "images/AlJunLightningKing.jpg";
	private static final String		defaultName		= "Al-Jun, Lightning King";
	private static final String		defaultText		= "Haste. Activate for 3 air: deal 3 to target enemy.";
	private static final int			defaultHealth	= 6;
	private static final int			defaultAttack	= 4;
	private static final CardType	defaultType		= CardType.CREATURE;


	public AlJunLightningKing(Player owner) {
		super(defaultHealth, defaultAttack, new ManaPool(80, 0, 0, 4, 0, 0), defaultImage, owner, defaultName, defaultText, defaultType);
	}
	
	public Effect onThisPlayed(Card c, Zone z){
		this.allowAttack();
		return EmptyEffect.create();
	}

	@Override
	public Effect onThisActivated() {
		//should never happen
		System.err.println("Al-Jun cant activate generic style");
		return null;
	}

	@Override
	public boolean canBeActivated(Zone z) {
		return z.equals(Zone.CREATURE_BOARD) && getOwner().validateCost(getActivationCost());
	}

	@Override
	public boolean cardValidTarget(Card card, Zone targetIn) {
		return targetIn.equals(Zone.CREATURE_BOARD);
	}

	@Override
	public Effect impactCardTarget(Card target, Zone targetIn) {
		return new CardDamageEffect(target,this,3);
	}

	@Override
	public boolean playerValidTarget(Player p) {
		return true;
	}
	
	
	public ManaPool getActivationCost(){
		return new ManaPool(0,0,0,0,3,0);
	}

	@Override
	public Effect impactPlayerTarget(Player target) {
		return new PlayerDamageEffect(target,this,3);
	}
	
	
}
