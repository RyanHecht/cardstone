package gui;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.google.common.collect.ImmutableMap;

import lobby.Lobby;
import lobby.LobbyManager;
import server.LobbyWebSocket;
import spark.ModelAndView;
import spark.QueryParamsMap;
import spark.Request;
import spark.Response;
import spark.Spark;
import spark.template.freemarker.FreeMarkerEngine;

public class LobbyRouter implements RouteGroup {
  private final FreeMarkerEngine fm;

  public LobbyRouter(FreeMarkerEngine fm) {
    this.fm = fm;
  }

  @Override
  public void registerRoutes() {
    Spark.get("/lobbies", new LobbiesHandler(), fm);
    Spark.post("/lobbies", new LobbiesHandler(), fm);
    Spark.get("/lobby", new LobbyHandler(), fm);
    Spark.post("/spectate", new SpectateHandler(), fm);
    Spark.get("/tutorial_lobby", new TutorialLobbyHandler(), fm);
  }

  private class LobbiesHandler implements AuthRoute {
    @Override
    public ModelAndView customHandle(Request req, Response res) {
      QueryParamsMap qm = req.queryMap();
      String errorMsg = qm.value("errorMsg");
      if (errorMsg == null) {
        return new ModelAndView(
            ImmutableMap.of("title", "Cardstone: The Shattering"),
            "lobbies.ftl");
      }

      String errorHead = qm.value("errorHead");
      Map<String, Object> vars = ImmutableMap.of("title",
          "Cardstone: The Shattering", "error", errorMsg, "errorHeader",
          errorHead);
      return new ModelAndView(vars, "lobbies.ftl");
    }
  }

  private class LobbyHandler implements AuthRoute {
    @Override
    public ModelAndView customHandle(Request req, Response res) {
      int uid = Integer.parseInt(req.cookie("id"));
      if (LobbyWebSocket.isConnected(uid)) {
        res.redirect("/lobbies");
      }

      Lobby l = LobbyManager.getLobbyByPlayerId(uid);
      if (l == null) {
        res.redirect("/lobbies");
      }

      System.out.println("Got lobby " + l);
      List<String> decks = new ArrayList<>();
      int oppId = l.getOtherPlayer(uid);
      String opp = "Opponent";
      String oppMsg = "Waiting for another player to join...";
      String deckQuery = "select name from deck where user = ?;";
      try (ResultSet rs = Db.query(deckQuery, uid)) {
        while (rs.next()) {
          decks.add(rs.getString(1));
        }
        if (oppId != -1) {
          try (ResultSet user = Db
              .query("select username from user where id = ?;", oppId)) {
            user.next();
            opp = user.getString(1);
            oppMsg = opp + " is choosing a deck";
            assert !user.next();
          }
        }
      } catch (NullPointerException | SQLException e) {
        e.printStackTrace();
      }

      Map<String, Object> vars = ImmutableMap.of("title",
          "Cardstone: The Shattering", "decks", decks, "isHost", l.isHost(uid),
          "opp", opp, "oppMsg", oppMsg);
      return new ModelAndView(vars, "lobby.ftl");
    }
  }

  private class SpectateHandler implements AuthRoute {
    @Override
    public ModelAndView customHandle(Request req, Response res) {
      String lname = req.queryMap().value("lobby");
      Lobby l = LobbyManager.getLobbyByName(lname);

      if (l == null) {
        res.redirect("/lobbies");
      }

      System.out.println("Got lobby " + l);
      int hostId = l.getHost();
      int otherId = l.getOtherPlayer(hostId);
      String hostUser = null;
      String otherUser = null;

      String userQuery = "select username from user where id = ? or id = ?;";
      try (ResultSet rs = Db.query(userQuery, hostId, otherId)) {
        rs.next();
        hostUser = rs.getString(1);
        if (rs.next()) {
          otherUser = rs.getString(1);
        }
        assert !rs.next();
      } catch (NullPointerException | SQLException e) {
        hostUser = "Player 1";
        otherUser = "Player 2";
      }

      Map<String, Object> vars;
      if (otherId == -1) {
        vars = new ImmutableMap.Builder<String, Object>()
            .put("title", "Cardstone: The Shattering").put("p1", hostUser)
            .put("p2", "Opponent").put("id1", hostId).put("id2", otherId)
            .put("msg", "Waiting for another player to join...").build();
      } else {
        vars = ImmutableMap.of("title", "Cardstone: The Shattering", "p1",
            hostUser, "p2", otherUser, "id1", hostId, "id2", otherId);
      }
      System.out.println(Arrays.toString(vars.values().toArray()));
      return new ModelAndView(vars, "spectate_lobby.ftl");
    }
  }

  private class TutorialLobbyHandler implements AuthRoute {
    @Override
    public ModelAndView customHandle(Request req, Response res) {
      return new ModelAndView(
          ImmutableMap.of("title", "Cardstone: The Shattering"),
          "tutorial_lobby.ftl");

    }
  }
}
