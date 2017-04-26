package logins;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
  private static final Gson GSON = new Gson();

  public Gui(FreeMarkerEngine fm) {
    Spark.get("/login", new LoginHandler(), fm);
    Spark.get("/register", new RegisterHandler(), fm);
    Spark.post("/login", new LoginHandler(), fm);
    
    Spark.post("/register", new RegisterHandler(), fm);
    Spark.post("/deck_upload", new UploadHandler());
    Spark.post("/menu", new MenuHandler(), fm);
    Spark.get("/menu", new MenuHandler(), fm);
    Spark.get("/decks", new DecksHandler(), fm); // displaying all decks
    Spark.get("/deck", new DeckHandler(), fm); // displaying one deck
    Spark.get("/games", new GameDisplayHandler(), fm);
    Spark.get("/replay", new GameReplayHandler(), fm);
  }

  public void init() {
    try {
      Db.update("create table if not exists user("
          + "id integer primary key autoincrement, "
          + "username text unique not null, password text not null);");
      Db.update("create table if not exists deck("
      		+ "id integer primary key autoincrement, "
      		+ "name text not null, "
      		+ "user integer not null, "
      		+ "cards text not null, "
      		+ "UNIQUE(name, user), "
      		+ "FOREIGN KEY (user) REFERENCES user(id) "
      		+ "ON DELETE CASCADE ON UPDATE CASCADE);");
      Db.update("create table if not exists game("
      		+ "id integer primary key autoincrement, "
      		+ "winner integer, moves integer, "
      		+ "UNIQUE(id, winner), "
      		+ "FOREIGN KEY (winner) REFERENCES user(id) "
      		+ "ON DELETE CASCADE ON UPDATE CASCADE);");
      Db.update("create table if not exists user_game("
      		+ "user integer not null, game integer not null,"
      		+ "UNIQUE(user, game),"
      		+ "FOREIGN KEY (user) REFERENCES user(id)"
      		+ "ON DELETE CASCADE ON UPDATE CASCADE,"
      		+ "FOREIGN KEY (game) REFERENCES game(id)"
      		+ "ON DELETE CASCADE ON UPDATE CASCADE);");
      Db.update("create table if not exists game_event("
      		+ "game integer not null, event integer not null,"
      		+ "board text not null, UNIQUE(game, event),"
      		+ "FOREIGN KEY (game) REFERENCES game(id)"
      		+ "ON DELETE CASCADE ON UPDATE CASCADE);");
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }
  
  private class GameReplayHandler implements TemplateViewRoute {
	  @Override
	  public ModelAndView handle(Request req, Response res) {
		  String queryString = req.queryString();
		  String gameId = queryString.substring(queryString.lastIndexOf('=') + 1);
		  System.out.println("Have game id: " + gameId);
		  
		  String eventQuery = "select board from game_event where game = ?;";
		  List<String> events = new ArrayList<>();
		  try (ResultSet rs = Db.query(eventQuery, gameId)) {
			  while (rs.next()) {
				  events.add(rs.getString(1));
			  }
		  } catch (SQLException | NullPointerException e) {
			  e.printStackTrace();
		  }

		  Map<String, Object> vars = ImmutableMap.of("title",
		          "Cardstone: The Shattering", "username", 
		          req.cookie("username"), "events", events);
		  return new ModelAndView(vars, "replay.ftl");
	  }
  }
  
  private class GameDisplayHandler implements TemplateViewRoute {
	  @Override
	  public ModelAndView handle(Request req, Response res) {
		  String uid = req.cookie("id");
		  
		  String gameQuery = "select g.id, g.winner, g.moves from game as g, user_game"
		  		+ " as ug where g.id = ug.game and ug.user = ?;";
		  List<MetaGame> toDisplay = new ArrayList<>();
		  try (ResultSet rs = Db.query(gameQuery, uid)) {
			  while (rs.next()) {
				  toDisplay.add(new MetaGame(rs.getInt(1), 
						  rs.getInt(2), rs.getInt(3)));
			  }
		  } catch (SQLException | NullPointerException e) {
			  e.printStackTrace();
		  }

		  Map<String, Object> vars = ImmutableMap.of("title",
		          "Cardstone: The Shattering", "username", 
		          req.cookie("username"), "games", toDisplay);
		  return new ModelAndView(vars, "games.ftl");
	  }
  }
  

  private class MenuHandler implements TemplateViewRoute {
	  @Override
	  public ModelAndView handle(Request req, Response res) {
		  String username = req.cookie("username");
		  System.out.println("Have username: " + username);
		  Map<String, Object> vars = ImmutableMap.of("title",
		          "Cardstone: The Shattering", "username", username);
		  return new ModelAndView(vars, "menu.ftl");
	  }
  }
  
  private class DecksHandler implements TemplateViewRoute {
	  @Override
	  public ModelAndView handle(Request req, Response res) {
		  String userId = req.cookie("id");
		  String username = req.cookie("username");
		  
		  List<String> decks = new ArrayList<>();
		  try (ResultSet rs = Db.query("select name from deck where user=?;", userId)) {
			while (rs.next()) {
				decks.add(rs.getString(1));
			}
		  } catch (NullPointerException | SQLException e) {
			e.printStackTrace();
		  } 
		  
		  System.out.println("Have username: " + username);
		  Map<String, Object> vars = ImmutableMap.of("title",
		          "Cardstone: The Shattering", "username", username, "decks", decks);
		  return new ModelAndView(vars, "decks.ftl");
	  }
  }
  
  private class DeckHandler implements TemplateViewRoute {
	  @Override
	  public ModelAndView handle(Request req, Response res) {
		  String userId = req.cookie("id");
		  String queryString = req.queryString();
		  String deckName = queryString.substring(queryString.lastIndexOf('=') + 1);
		  String cards = "Deck " + deckName + " doesn't exist.";
		  
		  System.out.println("Deck name is: " + deckName);
		  
		  String deckQuery = "select cards from deck where user=? and name=?;";
		  try (ResultSet rs = Db.query(deckQuery, userId, deckName)) {
			rs.next();
			cards = rs.getString(1);
			
			assert !rs.next();
		  } catch (NullPointerException | SQLException e) {
			e.printStackTrace();
		  } 
		  
		  Map<String, Object> vars = ImmutableMap.of("title",
		          "Cardstone: The Shattering", "deckname", deckName, "cards", cards);
		  return new ModelAndView(vars, "deck.ftl");
	  }
  }

  /**
   * Handles deck uploads.
   * @wriley1
   */
  private class UploadHandler implements Route {
    @Override
    public String handle(Request req, Response res) {
      QueryParamsMap qm = req.queryMap();
      
      String deck = qm.value("deck");
      String deckName = qm.value("name");
      String uid = req.cookie("id");
      System.out.println("Inserting muh deck " + deck);
      
      String deckSearch = "select id from deck where name = ? and user = ?;";
      try (ResultSet rs = Db.query(deckSearch, deckName, uid)) {
    	  if (rs.next()) {
    		  Db.update("update deck set cards = ? where id = ?;", rs.getInt(1));
    		  System.out.println("Overrode previous deck");
    	  } else {
    		  Db.update("insert into deck values(null, ?, ?, ?);", deckName, uid, deck);
    		  System.out.println("Inserted deck");
    	  }
      } catch (NullPointerException | SQLException e) {
		e.printStackTrace();
	  } 

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
      username = username == null ? "" : username.trim();
      String password = qm.value("password");

      Map<String, Object> vars = new HashMap<>();
      vars.put("title", "Cardstone: The Shattering");

      // See if there's anyone with the same username/password combo
      // if there is, log them in; otherwise, do not
      String loginQuery = "SELECT * FROM user WHERE username = ? "
              + "AND password = ?;";
      try (ResultSet rs = Db.query(loginQuery, username, password)) {
        if (!rs.next()) {
          return new ModelAndView(Collections.unmodifiableMap(vars),
              "login.ftl");
        }
        
        if (!username.isEmpty()) {
      	  res.cookie("username", username);
      	  String uid = rs.getString(1);
      	  System.out.println(uid);
      	  res.cookie("id", uid);
        }

        return new ModelAndView(Collections.unmodifiableMap(vars),
            "menu_redirect.ftl");
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

      String insertion = "insert into user values(null, ?, ?);";
      Map<String, Object> vars = ImmutableMap.of("title",
          "Cardstone: The Shattering", "username", username);

      // Try to make this username/password combo
      // if there's no issue, allow them through; otherwise,
      // if there's someone with that username already, or
      // some other error, keep them at the login screen
      System.out.println("I'm here with password " + password);
      try {
        Db.update(insertion, username, password);
        res.cookie("username", username);
        
        ResultSet rs = Db.query("select id from user where username = ?;", username);
        assert rs.next(); 
        String uid = rs.getString(1);
        rs.close();
        System.out.println("User id: " + uid);
        res.cookie("id", uid);
        return new ModelAndView(vars, "menu_redirect.ftl");
      } catch (SQLException | NullPointerException e) {
        e.printStackTrace();
        return new ModelAndView(vars, "login.ftl");
      }
    }
  }
}

