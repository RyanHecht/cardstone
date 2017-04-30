package lobby;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
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
    public String handle(Request req, Response res) throws Exception {
      QueryParamsMap qm = req.queryMap();
      String name = qm.value("name");
      boolean priv = Boolean.parseBoolean(qm.value("private"));
      String password = qm.value("password");

      JsonObject json = new JsonObject();
      boolean auth = false;
      System.out
          .println(String.format("Name: %s, Private: %s, Password: %s, UID: %d",
              name, priv, password, Integer.parseInt(req.cookie("id"))));
      try {
        int uid = Integer.parseInt(req.cookie("id"));
        LobbyManager.addLobby(name, priv, password, uid);
        auth = true;
      } catch (IllegalArgumentException e) {
        json.addProperty("message", e.getMessage());
      }
      json.addProperty("auth", auth);

      System.out.println(json.toString());
      return json.toString();
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
    public String handle(Request req, Response res) throws Exception {
      JsonObject obj = new JsonObject();
      QueryParamsMap qm = req.queryMap();
      String name = qm.value("name");
      String password = qm.value("password");

      boolean auth = false;
      try {
        System.out.println("Joining " + name + " with password " + password);
        LobbyManager.playerJoinLobby(Integer.parseInt(req.cookie("id")), name,
            password);
        auth = true;
      } catch (IllegalArgumentException e) {
        e.printStackTrace();
        obj.addProperty("message", e.getMessage());
      }
      obj.addProperty("auth", auth);
      System.out.println(obj.toString());
      return obj.toString();
    }

  }

  public static class SpectateJoinLobby implements Route {

    public SpectateJoinLobby() {

    }

    @Override
    public String handle(Request req, Response res) throws Exception {
      JsonObject obj = new JsonObject();
      QueryParamsMap qm = req.queryMap();
      String name = qm.value("name");
      String password = qm.value("password");

      boolean auth = false;
      try {
        System.out.println(
            "Joining " + name + " as spectator with password " + password);
        LobbyManager.spectatorJoinLobby(Integer.parseInt(req.cookie("id")),
            name, password);
        auth = true;
      } catch (IllegalArgumentException e) {
        e.printStackTrace();
        obj.addProperty("message", e.getMessage());
      }
      obj.addProperty("auth", auth);
      System.out.println(obj.toString());
      return obj.toString();
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
