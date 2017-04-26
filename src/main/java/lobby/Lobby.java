package lobby;

import cardgamelibrary.Jsonifiable;
import com.google.gson.JsonObject;
import game.Game;
import game.GameManager;
import java.util.List;

public class Lobby implements Jsonifiable {
  private String name;
  private boolean priv;
  private String password;
  private Integer uId1;
  private Integer uId2;
  private List<String> p1deck;
  private List<String> p2deck;

  public Lobby(String name, boolean priv, String password, int hostUId) {
    this.name = name;
    this.priv = priv;
    this.password = password;
    this.uId1 = hostUId;
  }

  public Lobby(String name, int hostUId) {
    this.name = name;
    priv = false;
    password = null;
    this.uId1 = hostUId;
  }

  public boolean isPrivate() {
    return priv;
  }

  public String getPassword() {
    return password;
  }

  public boolean isFull() {
    return (uId1 != null) && (uId2 != null);
  }

  public boolean decksSet() {
    return (p1deck != null) && (p2deck != null);
  }

  public int getCount() {
    if (isFull()) {
      return 2;
    } else {
      return 1;
    }
  }

  public void join(int uId) {
    if (!isFull()) {
      uId2 = uId;
    } else {
      throw new IllegalArgumentException("Cannot join full room.");
    }
  }

  public void leave(int uId) {
    if (uId == uId1) {
      LobbyManager.cancelLobby(this.name);
    } else if (uId == uId2) {
      // TODO: Handle player 2 leaving
    }
  }

  public void beginGame() {
    if (isFull() && decksSet()) {
      Game game = new Game(p1deck, p2deck, uId1, uId2);
      GameManager.addGame(uId1, uId2, game);
    }
  }

  public boolean containsPlayer(Integer uId) {
    return uId1 == uId || uId2 == uId;
  }

  public void setDeck(int uId, List<String> deck) {
    if (uId == uId1) {
      p1deck = deck;
    } else if (uId == uId2) {
      p2deck = deck;
    }
  }

  @Override
  public JsonObject jsonifySelf() {
    JsonObject obj = new JsonObject();
    obj.addProperty("name", this.name);
    obj.addProperty("count", getCount());
    obj.addProperty("isPrivate", priv);

    return obj;
  }

  @Override
  public JsonObject jsonifySelfChanged() {
    throw new UnsupportedOperationException();
  }

}
