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

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import game.Game;
import game.GameManager;
import lobby.Lobby;
import lobby.LobbyManager;
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
    Spark.post("/register", new RegisterHandler(), fm); // buggy
    Spark.post("/username", new UsernameHandler());
    
    
    Spark.post("/menu", new MenuHandler(), fm);
    Spark.get("/menu", new MenuHandler(), fm);
    
    Spark.post("/deck_upload", new UploadHandler());
    Spark.get("/decks", new DecksHandler(), fm); // displaying all decks
    Spark.get("/deck", new DeckHandler(), fm); // displaying one deck
    Spark.post("/deck_from", new DeckFinder());
    
    Spark.get("/games", new GameDisplayHandler(), fm);
    Spark.get("/replay", new GameReplayHandler(), fm);
    
    Spark.get("/lobbies", new LobbiesHandler(), fm);
    Spark.post("/lobbies", new LobbiesHandler(), fm);
    Spark.get("/lobby", new LobbyHandler(), fm);
    
    
    
    Spark.post("/spectate", new SpectateHandler(), fm);
    Spark.get("/game", new GameHandler(), fm);
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
      Db.update("create table if not exists finished_games("
      		+ "id integer primary key, "
      		+ "winner integer, moves integer, "
      		+ "UNIQUE(id, winner), "
      		+ "FOREIGN KEY (winner) REFERENCES user(id) "
      		+ "ON DELETE CASCADE ON UPDATE CASCADE);");
      Db.update("create table if not exists all_games("
      		+ "id integer primary key autoincrement,"
      		+ "player1 integer, player2 integer, "
      		+ "FOREIGN KEY (player1) REFERENCES user(id) "
      		+ "ON DELETE CASCADE ON UPDATE CASCADE,"
      		+ "FOREIGN KEY (player2) REFERENCES user(id)"
      		+ "ON DELETE CASCADE ON UPDATE CASCADE, "
      		+ "UNIQUE(id, player1, player2));");
      Db.update("create table if not exists user_game("
      		+ "user integer not null, game integer not null,"
      		+ "UNIQUE(user, game),"
      		+ "FOREIGN KEY (user) REFERENCES user(id)"
      		+ "ON DELETE CASCADE ON UPDATE CASCADE,"
      		+ "FOREIGN KEY (game) REFERENCES finished_game(id)"
      		+ "ON DELETE CASCADE ON UPDATE CASCADE);");
      Db.update("create table if not exists game_event("
      		+ "game integer not null, event integer not null,"
      		+ "board text not null, UNIQUE(game, event),"
      		+ "FOREIGN KEY (game) REFERENCES all_games(id)"
      		+ "ON DELETE CASCADE ON UPDATE CASCADE);");
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  /**
   * Handles requests to the lobby page.
   * @author wriley1
   */
  private class GameHandler implements TemplateViewRoute {
	  @Override
	  public ModelAndView handle(Request req, Response res) {
		  System.out.println("Gabbagoo");
		  
		  int uid = Integer.parseInt(req.cookie("id"));
		  Game g = GameManager.getGameByPlayerId(uid);
		  if (g == null) {
			return new ModelAndView(ImmutableMap.of("title", "Cardstone: The Shattering", 
					"error", "You are not currently in a game. Join one on this page.", 
					"errorHeader", "Could not play game"), "lobbies.ftl");
		  }

		  return new ModelAndView(ImmutableMap.of(), "boardDraw.ftl");
	  }
  }

  /**
   * Handles requests to spectate.
   * @author wriley1
   */
  private class SpectateHandler implements TemplateViewRoute {
	  @Override
	  public ModelAndView handle(Request req, Response res) {
		  System.out.println("I IS SPECTATIN AND HANDLIN");
		  String lname = req.queryMap().value("lobby");
		  Lobby l = LobbyManager.getLobbyByName(lname);
		  
		  if (l == null) {
			  return new ModelAndView(ImmutableMap.of("title", "Cardstone: The Shattering", 
					  "error", "Please find another lobby", "errorHeader", 
					  "Lobby " + lname + " does not exist"), "lobbies.ftl");
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
					.put("title", "Cardstone: The Shattering")
					.put("p1", hostUser)
					.put("p2", "Opponent")
					.put("id1", hostId)
					.put("id2", otherId)
					.put("msg", "Waiting for another player to join...")
					.build();
		  } else {
			vars = ImmutableMap.of("title", "Cardstone: The Shattering", 
						"p1", hostUser, "p2", otherUser, "id1", hostId, 
						"id2", otherId);
		  }
		  System.out.println(Arrays.toString(vars.values().toArray()));
		  return new ModelAndView(vars, "spectate_lobby.ftl");
	  }
  }
  
  /**
   * Handles requests to the lobby page.
   * @author wriley1
   */
  private class LobbyHandler implements TemplateViewRoute {
	  @Override
	  public ModelAndView handle(Request req, Response res) {
		  int uid = Integer.parseInt(req.cookie("id"));
		  Lobby l = LobbyManager.getLobbyByPlayerId(uid);
		  
		  if (l == null) {
			  return new ModelAndView(ImmutableMap.of("title", "Cardstone: The Shattering"), 
					  "lobbies.ftl");
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
				try (ResultSet user = Db.query("select username from user where id = ?;", oppId)) {
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
		          "Cardstone: The Shattering", "decks", decks, 
		          "isHost", l.isHost(uid), "opp", opp, "oppMsg", oppMsg);
		  System.out.println(Arrays.toString(vars.values().toArray()));
		  return new ModelAndView(vars, "lobby.ftl");
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
		  
		  String gameQuery = "select g.id, g.winner, g.moves from finished_games as g, user_game"
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
				System.out.println("Adding " + rs.getString(1) + " to deck list");
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
		  
		  int deckIndex = queryString.lastIndexOf('=');
		  System.out.println(queryString + " has LI of " + deckIndex); 
		  if (deckIndex == -1) {
			  System.out.println("I's in here massah");
			  return new ModelAndView(ImmutableMap.of("title",
			          "Cardstone: The Shattering"), "deck.ftl");
		  }
		  
		  
		  String deckName = queryString.substring(deckIndex + 1).replace('_', ' ');
		  String cards = "Deck " + deckName + " doesn't exist.";  
		  System.out.println("Deck name is: " + deckName);
		  
		  String deckQuery = "select cards from deck where user=? and name=?;";
		  try (ResultSet rs = Db.query(deckQuery, userId, deckName)) {
			if (rs.next()) {
				cards = rs.getString(1);
			}
			
			assert !rs.next();
		  } catch (NullPointerException | SQLException e) {
			e.printStackTrace();
		  } 
		  
		  
		  Map<String, Object> vars = ImmutableMap.of("title",
		          "Cardstone: The Shattering", "deckname", deckName, "deck", cards);
		  System.out.println(Arrays.toString(vars.values().toArray()));
		  System.out.println(Arrays.toString(vars.keySet().toArray()));
		  return new ModelAndView(vars, "deck.ftl");
	  }
  }
  
  private class LobbiesHandler implements TemplateViewRoute {
	  @Override
	  public ModelAndView handle(Request req, Response res) {
		  QueryParamsMap qm = req.queryMap();
		  String errorMsg = qm.value("errorMsg");
		  
		  if (errorMsg == null) {
			  return new ModelAndView(
					  ImmutableMap.of("title", "Cardstone: The Shattering"), 
					  "lobbies.ftl");
		  }
		  
		  String errorHead = qm.value("errorHead");
		  
		  Map<String, Object> vars = ImmutableMap.of("title",
		          "Cardstone: The Shattering", "error", errorMsg, 
		          "errorHeader", errorHead);
		  System.out.println(Arrays.toString(vars.values().toArray()));
		  return new ModelAndView(vars, "lobbies.ftl");
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
      System.out.println("Have deck named " + deckName);
      System.out.println("Inserting muh deck " + deck);
      
      String deckSearch = "select id from deck where name = ? and user = ?;";
      try (ResultSet rs = Db.query(deckSearch, deckName, uid)) {
    	  if (rs.next()) {
    		  Db.update("update deck set cards = ? where id = ?;", deck, rs.getInt(1));
    		  System.out.println("Overrode previous deck " + deckName);
    	  } else {
    		  Db.update("insert into deck values(null, ?, ?, ?);", deckName, uid, deck);
    		  System.out.println("Inserted deck " + deckName);
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
   * Finds username based on user id.
   * @wriley1
   */
  private class UsernameHandler implements Route {
    @Override
    public String handle(Request req, Response res) {
      JsonObject json = new JsonObject();
      QueryParamsMap qm = req.queryMap();
      
      String id = qm.value("id");
      String username;
      System.out.println("Have id: " + id);
      String userQuery = "select username from user where id = ?;";
      try (ResultSet rs = Db.query(userQuery, id)) {
    	  rs.next();
    	  username = rs.getString(1);
    	  assert !rs.next();
      } catch (NullPointerException | SQLException e) {
    	  username = "Anonymous";
    	  e.printStackTrace();
      }
      
      json.addProperty("username", username);
      return json.toString();
    }
  }
  
  /**
   * Finds cards in deck given deck's name.
   * @wriley1
   */
  private class DeckFinder implements Route {
    @Override
    public String handle(Request req, Response res) {
      JsonObject json = new JsonObject();
      QueryParamsMap qm = req.queryMap();
      
      String deckName = qm.value("deck");
      String uid = req.cookie("id");
      System.out.println("Have deckname: " + deckName + " " + deckName.length());
      String deckQuery = "select cards from deck where name = ? and user = ?;";
      try (ResultSet rs = Db.query(deckQuery, deckName, uid)) {
    	  rs.next();
    	  json.addProperty("cards", rs.getString(1));
    	  assert !rs.next();
      } catch (NullPointerException | SQLException e) {
    	  e.printStackTrace();
      }

      return json.toString();
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

