package gui;

import java.util.List;

import com.google.common.collect.ImmutableList;

import spark.template.freemarker.FreeMarkerEngine;

/**
 * For the GUI and such.
 * @author wriley1
 */
public class Gui implements RouteGroup {
  private final List<RouteGroup> routes;

  public Gui(FreeMarkerEngine fm) {
    routes = ImmutableList.of(
        new LoginHandler(fm), new DeckRouter(fm),
        new ReplayRouter(fm), new LobbyRouter(fm), 
        new GameRouter(fm), new MenuHandler(fm));

    Db.init();
    registerRoutes();
  }

  @Override
  public void registerRoutes() {
    for (RouteGroup r : routes) {
      r.registerRoutes();
    }
  }
}
