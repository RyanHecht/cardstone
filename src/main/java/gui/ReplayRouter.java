package gui;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import com.google.common.collect.ImmutableMap;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import game.GameManager;
import game.MetaGame;
import game.ReplayEvent;
import spark.ModelAndView;
import spark.QueryParamsMap;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.Spark;
import spark.template.freemarker.FreeMarkerEngine;

public class ReplayRouter implements RouteGroup {
  private final FreeMarkerEngine fm;

  public ReplayRouter(FreeMarkerEngine fm) {
    this.fm = fm;
  }

  @Override
  public void registerRoutes() {
    Spark.get("/replays", new ReplaysHandler(), fm);
    Spark.get("/replay", new ReplayHandler(), fm);
    Spark.post("/replay", new ReplayEventHandler());
    Spark.post("/user_replays", new GameGetter());
  }

  private static class ReplaysHandler implements AuthRoute {
    @Override
    public ModelAndView customHandle(Request req, Response res) {
      Map<String, Object> vars = ImmutableMap.of("title",
          "Cardstone: The Shattering", "username", req.cookie("username"));
      return new ModelAndView(vars, "games.ftl");
    }
  }

  // Finds all of a user's replays, returning them in a displayable fashion
  private static class GameGetter implements Route {
    @Override
    public Object handle(Request req, Response res) {
      QueryParamsMap qm = req.queryMap();
      int user = Integer.parseInt(qm.value("user"));

      String gameQuery = "select * from finished_game where player1 = ? or player2 = ?;";
      JsonArray games = new JsonArray();
      try (ResultSet rs = Db.query(gameQuery, user, user)) {
        while (rs.next()) {
          MetaGame toAdd = new MetaGame.MetaBuilder().id(rs.getInt(1))
              .winner(rs.getInt(2)).p1(rs.getInt(3)).p2(rs.getInt(4))
              .moves(rs.getInt(5)).date(rs.getString(6)).build(user);
          games.add(toAdd.jsonifySelf());
        }
      } catch (SQLException | NullPointerException e) {
        e.printStackTrace();
      }
      return games;
    }
  }

  private static class ReplayHandler implements AuthRoute {
    @Override
    public ModelAndView customHandle(Request req, Response res) {
      String queryString = req.queryString();
      String gameId = queryString.substring(queryString.lastIndexOf('=') + 1);

      Map<String, Object> vars = ImmutableMap.of("gameId", gameId, "isReplay",
          true);
      return new ModelAndView(vars, "boardDraw.ftl");
    }
  }

  private class ReplayEventHandler implements Route {
    @Override
    public String handle(Request req, Response res) {
      QueryParamsMap qm = req.queryMap();
      int game = Integer.parseInt(qm.value("gameId"));
      int event = Integer.parseInt(qm.value("eventNum"));

      JsonObject response = new JsonObject();

      ReplayEvent gameState = GameManager.boardFrom(game, event);
      if (gameState != null) {
        response.add("board", gameState.getBoard());
        response.add("animations", gameState.getAnimations());
      }
      response.addProperty("exists", gameState != null);

      return response.toString();
    }
  }
}
