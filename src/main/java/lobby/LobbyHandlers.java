package lobby;

import java.util.Arrays;
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

  public static class LobbyTesting implements Route {

    @Override
    public Object handle(Request req, Response res) throws Exception {
      if (LobbyManager.playerIsInLobby(2)) {
        System.out.println("will's in a lobby.");
      } else {
        LobbyManager.addLobby("test", false, "", 2);
        LobbyManager.playerJoinLobby(4, "test");
        LobbyManager.getLobbyByName("test").setDeck(2,
            Arrays.asList("EarthswornObserver",
                "BuriedTreasure"));
        LobbyManager.getLobbyByName("test").setDeck(4,
            Arrays.asList("EarthswornObserver",
                "EarthswornObserver"));
        LobbyManager.getLobbyByName("test").beginGame();
        System.out.println("made lobby");
      }

      res.redirect("/boardDraw.html");
      System.out.println("redirected");
      return null;
    }

  }
}
