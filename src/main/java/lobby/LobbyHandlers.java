package lobby;

import com.google.gson.JsonArray;
import java.util.Arrays;
import spark.QueryParamsMap;
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
      JsonArray array = new JsonArray();
      for (Lobby l : LobbyManager.getAllLobbies()) {
        array.add(l.jsonifySelf());
      }
      return array;
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
      QueryParamsMap qm = req.queryMap();
      String name = qm.value("name");
      boolean priv = Boolean.parseBoolean(qm.value("private"));
      String password = qm.value("password");

      try {
        LobbyManager.addLobby(name, priv, password,
            Integer.parseInt(req.cookie("id")));

        // TODO: load lobby page
      } catch (IllegalArgumentException x) {
        // TODO: redirect to lobby list page, with message.
      }
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
      QueryParamsMap qm = req.queryMap();
      String name = qm.value("name");
      String password = qm.value("password");

      try {
        LobbyManager.playerJoinLobby(Integer.parseInt(req.cookie("id")), name,
            password);
        // TODO: load lobby page
      } catch (IllegalArgumentException x) {
        // TODO: redirect to lobby list page, with message.
      }
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
        LobbyManager.playerJoinLobby(4, "test", "");
        LobbyManager.getLobbyByName("test").setDeck(2,
            Arrays.asList("EarthswornObserver",
                "BuriedTreasure", "EarthElement", "EarthElement",
                "CheapJoshCreature", "CheapJoshCreature", "CheapJoshCreature",
                "CheapJoshCreature",
                "BuriedTreasure", "BuriedTreasure", "BuriedTreasure",
                "BuriedTreasure"));
        LobbyManager.getLobbyByName("test").setDeck(4,
            Arrays.asList("EarthswornObserver",
                "EarthswornObserver", "EarthElement", "EarthElement",
                "CheapJoshCreature", "CheapJoshCreature", "CheapJoshCreature",
                "CheapJoshCreature",
                "EarthElement", "EarthElement", "EarthElement",
                "EarthElement"));
        LobbyManager.getLobbyByName("test").beginGame();
        System.out.println("made lobby");
      }

      res.redirect("/boardDraw.html");
      System.out.println("redirected");
      return null;
    }

  }
}
