package logins;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;

import spark.ModelAndView;
import spark.QueryParamsMap;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.Spark;
import spark.TemplateViewRoute;
import spark.template.freemarker.FreeMarkerEngine;

/**
 * For the GUI and such.
 * @author wriley1
 */
public class Gui {
  private final FreeMarkerEngine fm;
  private static final Gson GSON = new Gson();
  private static final AtomicInteger ID_COUNTER = new AtomicInteger();

  public Gui(FreeMarkerEngine fm) {
    this.fm = fm;
  }

  public void init() {
    Spark.get("/login", new LoginHandler(), fm);
    Spark.get("/register", new RegisterHandler(), fm);
    Spark.post("/login", new LoginHandler(), fm);
    Spark.post("/register", new RegisterHandler(), fm);
    Spark.post("/deck_upload", new UploadHandler());

    try {
      Db.update("create table if not exists user("
          + "id integer primary key autoincrement, "
          + "username text unique not null, password text not null);");
//      Db.update("create table if not exists game("
//              + "id integer primary key autoincrement, "
//              + "username text unique not null, password text not null);");
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  /**
   * Handles file uploads.
   * @wriley1
   */
  private class UploadHandler implements Route {
    @Override
    public String handle(Request req, Response res) {
      QueryParamsMap qm = req.queryMap();
      
//      String username = req.cookie("username");
      String file = qm.value("upload");
      System.out.println("Uploading muh file " + file);

      Map<String, Object> variables = ImmutableMap.of("title",
          "Cardstone: The Shattering");
      return GSON.toJson(variables);
    }
  }

  /**
   * Login page.
   * @author wriley1
   */
  private class LoginHandler implements TemplateViewRoute {
    @Override
    public ModelAndView handle(Request req, Response res)
        throws NullPointerException, IllegalArgumentException, IOException {
      QueryParamsMap qm = req.queryMap();
      String username = qm.value("username");
      username = username == null ? "" : username;
      String password = qm.value("password");

      String loginQuery = "SELECT * FROM user WHERE username = ? "
          + "AND password = ?;";
      Map<String, Object> vars = new HashMap<>();
      vars.put("title", "Cardstone: The Shattering");
      vars.put("username", username);


      // See if there's anyone with the same username/password combo
      // if there is, log them in; otherwise, do not
      try (ResultSet rs = Db.query(loginQuery, username, password)) {
        if (!rs.next()) {
          return new ModelAndView(Collections.unmodifiableMap(vars),
              "login.ftl");
        }

        return new ModelAndView(Collections.unmodifiableMap(vars),
            "menu.ftl");
      } catch (SQLException e) {
        e.printStackTrace();
        return new ModelAndView(Collections.unmodifiableMap(vars), "login.ftl");
      }
    }
  }

  /**
   * Creating accounts.
   * @author wriley1
   */
  private class RegisterHandler implements TemplateViewRoute {
    @Override
    public ModelAndView handle(Request req, Response res)
        throws NullPointerException, IllegalArgumentException, IOException {
      QueryParamsMap qm = req.queryMap();
      String username = qm.value("username");
      String password = qm.value("password");
      System.out.println("First username " + username);
      username = username == null ? "" : username;

      String insertion = "insert into user values(?, ?, ?);";
      Map<String, Object> vars = ImmutableMap.of("title",
          "Cardstone: The Shattering", "username", username, "puppies",
          Collections.emptyList());

      // Try to make this username/password combo
      // if there's no issue, allow them through; otherwise,
      // if there's someone with that username already, or
      // some other error, keep them at the login screen
      System.out.println("I'm here with password " + password);
      try {
        Db.update(insertion, ID_COUNTER.incrementAndGet(), username, password);
        System.out.println("Id: " + ID_COUNTER.get());
        return new ModelAndView(vars, "success.ftl");
      } catch (SQLException | NullPointerException e) {
        e.printStackTrace();
        return new ModelAndView(vars, "login.ftl");
      }
    }
  }
}

