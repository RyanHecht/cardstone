package gui;

import java.util.Arrays;
import java.util.List;

import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.TemplateViewRoute;

/**
 * An interface for a route that checks if a user
 * is logged in before displaying itself.
 * @author Willay
 *
 */
public interface AuthRoute extends TemplateViewRoute {

  default ModelAndView handle(Request req, Response res) {
    List<String> cookies = Arrays.asList(req.cookie("id"),
        req.cookie("username"), req.cookie("tutorial"));

    if (!authorized(cookies)) {
      res.redirect("/login");
    }
    return customHandle(req, res);
  }

  ModelAndView customHandle(Request req, Response res);

  default boolean authorized(List<String> cookies) {
    for (String cookie : cookies) {
      if (cookie == null) {
        return false;
      }
    }
    return true;
  }
}
