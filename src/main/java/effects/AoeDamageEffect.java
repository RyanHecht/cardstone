package effects;

import cardgamelibrary.Board;
import cardgamelibrary.Card;
import cardgamelibrary.CreatureInterface;
import cardgamelibrary.FunctionThree;
import game.Player;

public class AoeDamageEffect implements DamageInterface {

	private boolean damagesOwner;
	private boolean damagesOpponent;
	private Card src;
	private FunctionThree<Board, CreatureInterface, Boolean> checker;
	private FunctionThree<Board, CreatureInterface, Integer> determineCreatureDamage;
	private int damageAmp;
	private int damageOwner;
	private int damageOpponent;

	public AoeDamageEffect(int damage, int damageOwner, int damageOpponent, boolean damagesOwner, boolean damagesOpponent,
			Card src, FunctionThree<Board, CreatureInterface, Boolean> checker,
			FunctionThree<Board, CreatureInterface, Integer> determineCreatureDamage) {
		super();
		this.damageAmp = damage;
		this.damageOwner = damageOwner;
		this.damageOpponent = damageOpponent;
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
			board.damagePlayer(getSrc().getOwner(), getSrc(), damageOwner + damageAmp);
		}
		if (damagesOpponent) {
			Player opponent = board.getOpposingPlayer(getSrc().getOwner());
			board.damagePlayer(opponent, getSrc(), damageOpponent);
		}

		for (Card c : board.getPlayerOneCreatures()) {
			CreatureInterface cr = (CreatureInterface) c;
			if (checkCreature(checker, board, cr)) {
				board.damageCard(cr, getSrc(), getCreatureDamage(determineCreatureDamage, board, cr) + damageAmp);
			}
		}

		for (Card c : board.getPlayerTwoCreatures()) {
			CreatureInterface cr = (CreatureInterface) c;
			if (checkCreature(checker, board, cr)) {
				board.damageCard(cr, getSrc(), getCreatureDamage(determineCreatureDamage, board, cr) + damageAmp);
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

	public int getOwnerDamage() {
		return damageOwner;
	}

	public int getOpponentDamage() {
		return damageOpponent;
	}

	@Override
	public void setDamage(int dmg) {
		this.damageAmp = dmg;
	}

	@Override
	public int getDamage() {
		return damageAmp;
	}

}
