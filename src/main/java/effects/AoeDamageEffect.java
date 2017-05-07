package effects;

import cardgamelibrary.Board;
import cardgamelibrary.Card;
import cardgamelibrary.CreatureInterface;
import cardgamelibrary.FunctionThree;

public class AoeDamageEffect implements DamageInterface {

	private boolean damagesOwner;
	private boolean damagesOpponent;
	private Card src;
	private FunctionThree<Board, CreatureInterface, Boolean> checker;
	private FunctionThree<Board, CreatureInterface, Integer> determineCreatureDamage;
	private int damage;

	public AoeDamageEffect(int damage, boolean damagesOwner, boolean damagesOpponent, Card src,
			FunctionThree<Board, CreatureInterface, Boolean> checker,
			FunctionThree<Board, CreatureInterface, Integer> determineCreatureDamage) {
		super();
		this.damage = damage;
		this.damagesOwner = damagesOwner;
		this.damagesOpponent = damagesOpponent;
		this.src = src;
		this.checker = checker;
		this.determineCreatureDamage = determineCreatureDamage;
	}

	@Override
	public void apply(Board board) {
		// TODO Auto-generated method stub
		if (damagesOwner) {

		}
		if (damagesOpponent) {

		}

		for (Card c : board.getPlayerOneCreatures()) {
			CreatureInterface cr = (CreatureInterface) c;
			if (checkCreature(checker, board, cr)) {
				board.damageCard(cr, getSrc(), getCreatureDamage(determineCreatureDamage, board, cr) + damage);
			}
		}

		for (Card c : board.getPlayerTwoCreatures()) {
			CreatureInterface cr = (CreatureInterface) c;
			if (checkCreature(checker, board, cr)) {
				board.damageCard(cr, getSrc(), getCreatureDamage(determineCreatureDamage, board, cr) + damage);
			}
		}
	}

	@Override
	public EffectType getType() {
		return EffectType.AOE_DAMAGE;
	}

	@Override
	public Card getSrc() {
		return src;
	}

	public boolean checkCreature(FunctionThree<Board, CreatureInterface, Boolean> func, Board b, CreatureInterface cr) {
		return func.apply(b, cr);
	}

	public Integer getCreatureDamage(FunctionThree<Board, CreatureInterface, Integer> func, Board b,
			CreatureInterface cr) {
		return 1;
	}

	@Override
	public void setDamage(int dmg) {
		this.damage = dmg;
	}

	@Override
	public int getDamage() {
		return damage;
	}

}
