package logins;

import com.google.gson.JsonObject;

import cardgamelibrary.Jsonifiable;

/**
 * A simple class to display info about a game
 * to a user.
 * @author wriley1
 */
public class MetaGame implements Jsonifiable {
  private final int id;
  private final int winner;
  private final int opponent;
  private final int moves;
  private final String date;

  private MetaGame(MetaBuilder builder, int user) {
    this.id = builder.id;
    this.winner = builder.winner;
    this.opponent = user == builder.p1 ? builder.p2 : builder.p1;
    this.moves = builder.moves;
    this.date = builder.date;
  }

  public int getId() {
    return id;
  }

  public int getWinner() {
    return winner;
  }

  public int getMoves() {
    return moves;
  }

  public String getDate() {
    return date;
  }

  public int getOpponent() {
    return opponent;
  }

  @Override
  public JsonObject jsonifySelf() {
    JsonObject res = new JsonObject();
    res.addProperty("id", id);
    res.addProperty("winner", winner);
    res.addProperty("opponent", opponent);
    res.addProperty("moves", moves);
    res.addProperty("date", date);
    return res;
  }

  @Override
  public JsonObject jsonifySelfChanged() {
    throw new UnsupportedOperationException();
  }

  public static class MetaBuilder {
    private int id;
    private int winner;
    private int p1;
    private int p2;
    private int moves;
    private String date;

    public MetaBuilder() {
    }

    public MetaBuilder id(int id) {
      this.id = id;
      return this;
    }

    public MetaBuilder winner(int winner) {
      this.winner = winner;
      return this;
    }

    public MetaBuilder p1(int p1) {
      this.p1 = p1;
      return this;
    }

    public MetaBuilder p2(int p2) {
      this.p2 = p2;
      return this;
    }

    public MetaBuilder moves(int moves) {
      this.moves = moves;
      return this;
    }

    public MetaBuilder date(String date) {
      this.date = date;
      return this;
    }

    public MetaGame build(int requestingUser) {
      return new MetaGame(this, requestingUser);
    }
  }
}
