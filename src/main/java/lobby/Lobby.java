package lobby;

import cardgamelibrary.Jsonifiable;
import com.google.gson.JsonObject;

public class Lobby implements Jsonifiable {
  private String name;
  private boolean priv;
  private String password;
  private Integer uId1;
  private Integer uId2;

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

  public int getCount() {
    if (isFull()) {
      return 2;
    } else {
      return 1;
    }
  }

  public void join(int uId) {

  }

  public void leave(int uId) {
    if (uId == uId1) {
      LobbyManager.cancelLobby(this.name);
    } else if (uId == uId2) {
      // TODO: Handle player 2 leaving
    }
  }

  public void beginGame() {

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
