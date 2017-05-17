package cards;

import cardgamelibrary.Card;
import cardgamelibrary.CardType;
import cardgamelibrary.Creature;
import cardgamelibrary.Effect;
import cardgamelibrary.ManaPool;
import cardgamelibrary.Zone;
import effects.CardDamageEffect;
import effects.EmptyEffect;
import effects.PlayerDamageEffect;
import game.Player;
import templates.ActivateTargetingCard;
import templates.ActivateTargetingPlayer;

public class StormSprite extends Creature implements ActivateTargetingCard, ActivateTargetingPlayer{

	private static final String		defaultImage	= "images/StormSprite.png";
	private static final String		defaultName		= "Storm Sprite";
	private static final String		defaultText		= "Activate for 30 and 1 air and TAP: deal 2 to target enemy.";
	private static final int			defaultHealth	= 3;
	private static final int			defaultAttack	= 2;
	private static final CardType	defaultType		= CardType.CREATURE;


	public StormSprite(Player owner) {
		super(defaultHealth, defaultAttack, new ManaPool(20, 0, 0, 1, 0, 0), defaultImage, owner, defaultName, defaultText, defaultType);
	}
	
	public Effect onThisPlayed(Card c, Zone z){
		this.allowAttack();
		return EmptyEffect.create();
	}

	@Override
	public Effect onThisActivated() {
		//should never happen
		System.err.println("StormSprite cant activate generic style");
		return null;
	}

	@Override
	public boolean canBeActivated(Zone z) {
		return this.getNumAttacks() >= 1 && z.equals(Zone.CREATURE_BOARD) && getOwner().validateCost(getActivationCost());
	}

	@Override
	public boolean cardValidTarget(Card card, Zone targetIn) {
		return targetIn.equals(Zone.CREATURE_BOARD);
	}

	@Override
	public Effect impactCardTarget(Card target, Zone targetIn) {
		this.setAttacks(this.getNumAttacks() - 1);
		return new CardDamageEffect(target,this,3);
	}

	@Override
	public boolean playerValidTarget(Player p) {
		return true;
	}
	
	
	public ManaPool getActivationCost(){
		return new ManaPool(30,0,0,0,1,0);
	}

	@Override
	public Effect impactPlayerTarget(Player target) {
		this.setAttacks(this.getNumAttacks() - 1);
		return new PlayerDamageEffect(target,this,3);
	}
	
	
	
}
