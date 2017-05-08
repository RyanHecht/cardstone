package gui;

import java.util.Map;

import com.google.common.collect.ImmutableMap;

import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Spark;
import spark.template.freemarker.FreeMarkerEngine;

public class MenuHandler implements RouteGroup {
  private final FreeMarkerEngine fm;

  public MenuHandler(FreeMarkerEngine fm) {
    this.fm = fm;
  }

  @Override
  public void registerRoutes() {
    Spark.post("/menu", new MenuViewer(), fm);
    Spark.get("/menu", new MenuViewer(), fm);
  }

  private class MenuViewer implements AuthRoute {
    @Override
    public ModelAndView customHandle(Request req, Response res) {
      String username = req.cookie("username");
      Map<String, Object> vars = ImmutableMap.of("title",
          "Cardstone: The Shattering", "username", username);
      return new ModelAndView(vars, "menu.ftl");
    }
  }
}
