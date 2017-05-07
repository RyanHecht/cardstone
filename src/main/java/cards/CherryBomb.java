package cards;

import cardgamelibrary.Board;
import cardgamelibrary.CardType;
import cardgamelibrary.Creature;
import cardgamelibrary.Effect;
import cardgamelibrary.ManaPool;
import cardgamelibrary.Zone;
import effects.EffectMaker;
import effects.PlayerDamageEffect;
import game.Player;
import templates.OnOwnDeathCard;

public class CherryBomb extends Creature implements OnOwnDeathCard {

	private static final ManaPool defaultCost = new ManaPool(15, 1, 0, 0, 0, 0);
	private static final String defaultImage = "images/CherryBomb.jpg";
	private static final String defaultName = "Cherry Bomb";
	private static final String defaultText = "When this creature dies, your opponent takes 5 damage.";
	private static final int defaultHealth = 2;
	private static final int defaultAttack = 0;
	private static final CardType defaultType = CardType.CREATURE;

	public CherryBomb(Player owner) {
		super(defaultHealth, defaultAttack, defaultCost, defaultImage, owner, defaultName, defaultText, defaultType);
	}

	@Override
	public Effect onDeathEffect(Zone z) {
		// TODO Auto-generated method stub
		return new EffectMaker((Board board) -> {
			if(getOwner().equals(board.getActivePlayer())){
				return new PlayerDamageEffect(board.getInactivePlayer(),this,5);
			}
			else{
				return new PlayerDamageEffect(board.getActivePlayer(),this,5);
			}
		},this);
	}

}
