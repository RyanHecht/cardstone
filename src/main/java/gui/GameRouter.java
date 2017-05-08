package gui;

import com.google.common.collect.ImmutableMap;

import cardgamelibrary.MasterCardList;
import game.Game;
import game.GameManager;
import server.CommsWebSocket;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.Spark;
import spark.template.freemarker.FreeMarkerEngine;

public class GameRouter implements RouteGroup {
  private final FreeMarkerEngine fm;

  public GameRouter(FreeMarkerEngine fm) {
    this.fm = fm;
  }

  @Override
  public void registerRoutes() {
    Spark.get("/game", new GameHandler(), fm);
    Spark.get("/all_cards", new AllCardsHandler());
  }

  private class GameHandler implements AuthRoute {
    @Override
    public ModelAndView customHandle(Request req, Response res) {
      int uid = Integer.parseInt(req.cookie("id"));
      Game g = GameManager.getGameByPlayerId(uid);
      if (g == null) {
        if (!CommsWebSocket.isSpectator(uid)) {
          res.redirect("/lobbies");
          return new ModelAndView(
              ImmutableMap.of("title", "Cardstone: The Shattering", "error",
                  "You are not currently in a game. Join one on this page.",
                  "errorHeader", "Could not play game"),
              "lobbies.ftl");
        }
      }
      return new ModelAndView(ImmutableMap.of("isReplay", false),
          "boardDraw.ftl");
    }
  }

  private class AllCardsHandler implements Route {
    @Override
    public String handle(Request req, Response res) {
      return MasterCardList.master.getAllCards();
    }
  }
}
