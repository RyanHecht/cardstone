package lobby;

public class Lobby {
  private String name;
  private boolean priv;
  private String password;
  private Integer uId1;
  private Integer uId2;

  public Lobby(String name, boolean priv, String password, int hostUId) {

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
}
