package game;

public class GameStats {
  private final Game game;
  private final int numTurns;
  // 0 for tie.
  private final int winnerId;

  public GameStats(Game g, int winnerId) {
    game = g;
    this.winnerId = winnerId;
    numTurns = g.getNumTurns();
  }

  public Game getGame() {
    return game;
  }

  public int getNumTurns() {
    return numTurns;
  }

  public int getWinnerId() {
    return winnerId;
  }
}
