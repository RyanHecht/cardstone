package cardgamelibrary;

import effects.EffectType;
import effects.GateEffect;
import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedList;

public class ConcatEffect implements Effect, Serializable {

  private LinkedList<Effect> effects;

  private Card src;

  @Override
  public Card getSrc() {
    return src;
  }

  public ConcatEffect(Card src) {
    this.effects = new LinkedList<Effect>();
    this.src = src;
  }

  public void addEffect(Effect e) {
    effects.add(e);
  }

  @Override
  public void apply(Board board) {
    Iterator<Effect> it = effects.iterator();
    while (it.hasNext()) {
      Effect e = it.next();
      System.out.println(e.getType());
      if (e instanceof GateEffect) {
        e.apply(board);
        if (!((GateEffect) e).getShouldContinue()) {
          break;
        }
      }
      board.handleEffect(e);
    }
  }

  public Iterator<Effect> getEffects() {
    return effects.iterator();
  }

  @Override
  public EffectType getType() {
    return EffectType.CONCAT;
  }
}
