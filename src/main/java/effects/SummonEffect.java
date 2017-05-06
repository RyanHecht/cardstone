package effects;

import cardgamelibrary.Board;
import cardgamelibrary.Card;
import cardgamelibrary.Effect;
import cardgamelibrary.Zone;

public class SummonEffect implements Effect {

	private Card toSummon;
	private Zone targetZone;

	public SummonEffect(Card toSummon, Zone targetZone) {
		super();
		this.toSummon = toSummon;
		this.targetZone = targetZone;
	}

	@Override
	public void apply(Board board) {
		board.summonCard(toSummon, targetZone);
	}

	public Card getToSummon() {
		return toSummon;
	}

	public void setToSummon(Card toSummon) {
		this.toSummon = toSummon;
	}

	public Zone getTargetZone() {
		return targetZone;
	}

	public void setTargetZone(Zone targetZone) {
		this.targetZone = targetZone;
	}

	@Override
	public EffectType getType() {
		return EffectType.CARD_SUMMONED;
	}

}
