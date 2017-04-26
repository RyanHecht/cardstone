package lobby;

import spark.Request;
import spark.Response;
import spark.Route;

/**
 * Handlers for Spark pertaining to Lobbies.
 * @author ryan
 *
 */
public class LobbyHandlers {

  /**
   * Return a list of all lobbies.
   * @author ryan
   *
   */
  public static class ListLobbies implements Route {

    public ListLobbies() {

    }

    @Override
    public Object handle(Request req, Response res) throws Exception {
      // TODO Auto-generated method stub
      return null;
    }

  }

  /**
   * Make a lobby.
   * @author ryan
   *
   */
  public static class MakeLobby implements Route {

    public MakeLobby() {

    }

    @Override
    public Object handle(Request req, Response res) throws Exception {
      // TODO Auto-generated method stub
      return null;
    }

  }

  /**
   * Join a lobby.
   * @author ryan
   *
   */

  public static class JoinLobby implements Route {

    public JoinLobby() {

    }

    @Override
    public Object handle(Request req, Response res) throws Exception {
      // TODO Auto-generated method stub
      return null;
    }

  }
}
