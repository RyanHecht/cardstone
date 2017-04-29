package lobby;

import cardgamelibrary.Jsonifiable;
import com.google.gson.JsonObject;
import game.Game;
import game.GameManager;
import java.util.List;

public class Lobby implements Jsonifiable {
  private final String name;
  private final boolean priv;
  private final String password;
  private final Integer uId1;
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

  public void join(int uId, String pw) {
    if (priv) {
      if (!(this.password.equals(pw))) {
        throw new IllegalArgumentException("Incorrect password.");
      }
    }

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
      System.out.println("making game...");
      Game game = new Game(p1deck, p2deck, uId1, uId2);
      System.out.println("game made.");
      GameManager.addGame(uId1, uId2, game);
    }
  }

  public int getOtherPlayer(int uId) {
	System.out.println("Other player request from user " + uId);
	System.out.println("In lobby " + name + " with pw " + password + " and users " 
						+ uId1 + ", " + uId2);
	Integer ret = null;
    if (uId == uId1) {
      ret = uId2;
    } else if (uId == uId2) {
      ret = uId1;
    }
    
    return (ret == null) ? -1 : ret;
  }

  public boolean containsPlayer(Integer uId) {
    return uId1 == uId || uId2 == uId;
  }
  
  public boolean isHost(int uId) {
	  return uId1 == uId;
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
    obj.addProperty("full", getCount() > 1);
    obj.addProperty("private", priv);
    obj.addProperty("host", uId1);
    return obj;
  }

  @Override
  public JsonObject jsonifySelfChanged() {
    throw new UnsupportedOperationException();
  }

}
