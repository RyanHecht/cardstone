package gui;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.google.common.collect.ImmutableMap;
import com.google.common.hash.Hashing;
import com.google.gson.JsonObject;

import spark.ModelAndView;
import spark.QueryParamsMap;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.Session;
import spark.Spark;
import spark.TemplateViewRoute;
import spark.template.freemarker.FreeMarkerEngine;

public class LoginHandler implements RouteGroup {
  private final FreeMarkerEngine fm;
  private static Map<Integer, Session> usersToSessions = new ConcurrentHashMap<>();

  public LoginHandler(FreeMarkerEngine fm) {
    this.fm = fm;
  }

  @Override
  public void registerRoutes() {
    Spark.get("/login", new LoginViewer(), fm);
    Spark.post("/login", new LoginRequestHandler());
    Spark.post("/register", new RegisterHandler());
    Spark.post("/username", new UsernameGetter());
    Spark.get("/logout", new LogoutHandler(), fm);
    Spark.redirect.get("/", "/login");
  }

  private static class LogoutHandler implements AuthRoute {
    @Override
    public ModelAndView customHandle(Request req, Response res) {
      int uid = Integer.parseInt(req.cookie("id"));
      usersToSessions.remove(uid);
      return new ModelAndView(ImmutableMap.of("title", "Logged out"),
          "login.ftl");
    }
  }

  /**
   * For accessing the splash screen/login page.
   * @author wriley1
   */
  private static class LoginViewer implements TemplateViewRoute {
    @Override
    public ModelAndView handle(Request req, Response res)
        throws NullPointerException, IllegalArgumentException, IOException {
      return new ModelAndView(
          ImmutableMap.of("title", "Cardstone: The Shattering"), "login.ftl");
    }
  }

  /**
   * For registering accounts.
   * @author wriley1
   */
  private static class RegisterHandler implements Route {
    @Override
    public String handle(Request req, Response res)
        throws NullPointerException, IllegalArgumentException, IOException {
      QueryParamsMap qm = req.queryMap();
      String username = qm.value("username");
      String password = getSalted(qm.value("password"));
      username = username == null ? "" : username;

      JsonObject response = new JsonObject();
      boolean auth;
      String error = null;

      if (username.split("\\W+").length > 1) {
        response.addProperty("auth", false);
        response.addProperty("error",
            "Usernames must be one word of letters and numbers.");
        return response.toString();
      }

      try {
        Db.update("insert into user values(null, ?, ?);", username, password);

        try (ResultSet rs = Db.query("select id from user where username = ?;",
            username)) {
          assert rs.next();
          String uid = rs.getString(1);

          // set cookies
          res.cookie("username", username);
          res.cookie("id", uid);
          res.cookie("tutorial", "0"); // initiate tutorial
          auth = true;
        }
      } catch (SQLException | NullPointerException e) {
        e.printStackTrace();
        auth = false;
        error = "User with name " + username + " already exists.";
      }

      response.addProperty("auth", auth);
      if (error != null) {
        response.addProperty("error", error);
      }
      return response.toString();
    }
  }

  /**
   * For handling login requests.
   * @author Willay
   */
  private static class LoginRequestHandler implements Route {
    @Override
    public String handle(Request req, Response res)
        throws NullPointerException, IllegalArgumentException, IOException {
      QueryParamsMap qm = req.queryMap();
      String username = qm.value("username");
      username = username == null ? "" : username.trim();
      String password = qm.value("password");
      password = getSalted(password == null ? "" : password);

      JsonObject response = new JsonObject();
      boolean auth = false;
      String error = null;

      // See if there's anyone with the same username/password combo
      // if there is, log them in; otherwise, do not
      String loginQuery = "SELECT * FROM user WHERE username = ? "
          + "AND password = ?;";
      try (ResultSet rs = Db.query(loginQuery, username, password)) {
        if (rs.next()) {
          String user = rs.getString(1);
          int uid = Integer.parseInt(user);
          if (!usersToSessions.containsKey(uid)) {
            auth = true;
            res.cookie("username", username);
            res.cookie("id", user);
            res.cookie("tutorial", "-1");
            usersToSessions.put(uid, req.session());
          } else {
            error = username + " is currently logged in";
          }
        } else {
          error = "Invalid username or password";
        }
      } catch (SQLException e) {
        e.printStackTrace();
        auth = false;
        response.addProperty("error", "Invalid username or password.");
      }

      response.addProperty("auth", auth);
      if (error != null) {
        response.addProperty("error", error);
      }
      return response.toString();
    }
  }

  /**
   * Finds usernames based on user ids.
   * 
   * @author Willay
   */
  private static class UsernameGetter implements Route {
    @Override
    public String handle(Request req, Response res) {
      JsonObject json = new JsonObject();
      QueryParamsMap qm = req.queryMap();

      String id = qm.value("id");
      System.out.println("Trying to get username for id " + id);
      String username;
      String userQuery = "select username from user where id = ?;";
      try (ResultSet rs = Db.query(userQuery, id)) {
        username = rs.next() ? rs.getString(1) : "Anonymous";
      } catch (NullPointerException | SQLException e) {
        username = "Anonymous";
        e.printStackTrace();
      }

      json.addProperty("username", username);
      return json.toString();
    }
  }

  private static String getSalted(String password) {
    return Hashing.sha256().hashString(password, StandardCharsets.UTF_8)
        .toString();
  }

  public static boolean validSession(int user, Session s) {
    Session found = usersToSessions.get(user);
    return found != null ? s.id().equals(found.id()) : false;
  }
}
