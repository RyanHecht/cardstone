package tutorial;

import game.DemoGame;
import game.Game;
import game.GameManager;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.Spark;
import spark.template.freemarker.FreeMarkerEngine;

public class TutorialRoutes {

  public TutorialRoutes(FreeMarkerEngine fm) {
    Spark.get("/tutorialGame", new StartTutorialGame());
  }

  private static class StartTutorialGame implements Route {

    @Override
    public Object handle(Request req, Response res) throws Exception {
      int uid = Integer.parseInt(req.cookie("id"));

      Game demo = new DemoGame(uid);
      GameManager.addGame(demo);

      res.redirect("/game");
      return null;
    }

  }
}
