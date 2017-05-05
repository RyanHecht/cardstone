package game;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import logins.Db;
import server.CommsWebSocket;

/**
 * Class to keep track of all games currently running and handle playing them.
 * Can handle an arbitrary amount of games at once. Also stashes replay info.
 * @author Raghu and Willayyy
 */
public class GameManager {
  private static final Gson GSON = new Gson();
  private static GamePool games = new GamePool();
  private static Map<Integer, Integer> gamesToEventNums = new ConcurrentHashMap<>();
  private static Map<Integer, JsonArray> gamesToAnimations = new ConcurrentHashMap<>();

  // some sort of method to add games.
  public static void addGame(Game game) {
    if (games.updateGame(game)) {
      int gId = game.getId();
      try {
        conditionalInsert(game);
        gamesToEventNums.put(gId, 1);
        gamesToAnimations.put(gId, new JsonArray());
      } catch (IllegalStateException e) {
        e.printStackTrace();
      }
    }
  }

  // remove games when they complete.
  public static void endGame(GameStats ended) {
    Game g = ended.getGame();
    int gId = g.getId();
    int winner = ended.getWinnerId();
    int turns = ended.getNumTurns();

    int firstUser = g.getActivePlayerId();
    int secondUser = g.getOpposingPlayerId(firstUser);
    try {
      registerFinishedGame(gId, firstUser, secondUser, winner, turns);

      games.removeGame(firstUser);
      games.removeGame(secondUser);
      gamesToEventNums.remove(gId);
    } catch (SQLException | NullPointerException e) {
      throw new RuntimeException();
    }
  }

  public static boolean playerIsInGame(int playerId) {
    return games.getGameByPlayerId(playerId) != null;
  }

  public static Game getGameByPlayerId(int playerId) {
    return games.getGameByPlayerId(playerId);
  }

  public static void receiveUnderstoodBoardState(int playerId,
      JsonObject message) {
    Game game = games.getGameByPlayerId(playerId);
    if (game != null) {
      // game.h
    }
  }
  
  public static void receiveTargetedCard(int playerId, JsonObject message) {
    Game game = games.getGameByPlayerId(playerId);
    if (game != null) {
      game.handleCardTargeted(message, playerId);
    }
  }

  public static void receiveTargetedPlayer(int playerId, JsonObject message) {
    Game game = games.getGameByPlayerId(playerId);
    if (game != null) {
      game.handlePlayerTargeted(message, playerId);
    }
  }

  public static void receiveAttemptedToPlay(int playerId, JsonObject message) {
    System.out.println("got attempt to play.");
    Game game = games.getGameByPlayerId(playerId);
    if (game != null) {
      game.handleCardPlayed(message, playerId);
    }
  }

  public static void receiveChooseResponse(int playerId, JsonObject message) {
    Game game = games.getGameByPlayerId(playerId);
    if (game != null) {
      game.handleChosen(message, playerId);
    }
  }

  public static void receiveTurnEnd(int playerId) {
    Game game = games.getGameByPlayerId(playerId);
    if (game != null) {
      game.handleTurnend(null, playerId);
      System.out.println("handled turn end.");
    }
  }

  public static void receivePlayerChat(int playerId, JsonObject message) {
    String chat = message.get("message").getAsString();
    Game game = games.getGameByPlayerId(playerId);
    if (game != null) {
      int idToSend = game.getOpposingPlayerId(playerId);
      try {
        CommsWebSocket.sendChatMessage(idToSend, chat);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  public static void playerIsReady(int uId) {
    try {
      Game game = games.getGameByPlayerId(uId);
      CommsWebSocket.sendWholeBoardSate(game, uId);
      CommsWebSocket.sendTurnStart(uId, game.isActivePlayer(uId));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Caches board states and inserts their JSON into the Db for replay purposes.
   * @param g the game
   */
  public static void pushToDb(Game g) {
    // add game to cache
    if (games.updateGame(g)) {
      System.out.println("Successfully updated game state");
      String eventInsert = "insert into game_event values(?, ?, ?, ?);";
      int gId = g.getId();
      int eventNum = gamesToEventNums.get(gId);
      // insert JSON board state and animations into Db for replay purposes
      // and increment the event number
      try {
        System.out.println(
            "Trying to insert event num " + eventNum + " for game " + gId);
        JsonArray anims = gamesToAnimations.get(gId);
        String animString = anims == null ? "null" : anims.toString();
        System.out.println("Have some fuckin animations: " + animString);
        System.out.println("Also have event " + g.jsonifySelf());

        Db.update(eventInsert, gId, eventNum, g.jsonifySelf(),
            anims);
        gamesToEventNums.put(gId, ++eventNum);
        gamesToAnimations.replace(gId, new JsonArray());
        System.out.println("Event num now " + gamesToEventNums.get(gId));
      } catch (SQLException | NullPointerException e) {
        System.out.println(String.format("Game %d event %d is a duplicate",
            g.getId(), gamesToEventNums.get(g.getId())));
        e.printStackTrace();
      }
    }
  }

  /**
   * Returns the board state for a game event as a JsonObject.
   * @param gameId the game id.
   * @param eventNum the event number
   * @return a JsonObject of gameId's board state at eventNum
   */
  public static ReplayEvent boardFrom(int gameId, int eventNum) {
    String eventQuery = "select board, animations from game_event where "
        + "game = ? and event = ?;";
    try (ResultSet rs = Db.query(eventQuery, gameId, eventNum)) {
      rs.next();
      String boardString = rs.getString(1);
      String animationString = rs.getString(2);
      assert !rs.next();

      JsonObject board = GSON.fromJson(boardString, JsonObject.class);
      JsonArray animations = GSON.fromJson(animationString, JsonArray.class);
      return new ReplayEvent(board, animations);
    } catch (SQLException | NullPointerException e) {
      return null;
    }
  }

  public static int getStartingId() {
    int ret;
    try (ResultSet rs = Db.query("select max(id) from in_progress;")) {
      ret = rs.getInt(1);
    } catch (SQLException | NullPointerException e) {
      ret = 1;
    }
    System.out.println("Recommended starting id " + ret);
    return ret;
  }

  public static void addAnim(JsonObject anim, int gameId) {
    JsonArray anims = gamesToAnimations.get(gameId);
    String animString = anims == null ? "null" : anims.toString();
    System.out.println("Have some fuckin anims: " + animString);
    System.out.println("Adding animation " + anim.toString());
    if (anims == null) {
      anims = new JsonArray();
      gamesToAnimations.put(gameId, anims);
    }
    anims.add(anim);
  }

  // Transitions game from in_progress to finished_game
  public static void registerFinishedGame(int gId, int p1, int p2, int winner,
      int turns) throws NullPointerException, SQLException {
    System.out.println(String
        .format("Trying to stash %d with players %d and %d", gId, p1, p2));
    Db.update("delete from in_progress where id = ?;", gId);
    Db.update("insert into finished_game values(?, ?, ?);", gId, winner, turns);
    Db.update("insert into user_game values(?, ?);", p1, gId);
    Db.update("insert into user_game values(?, ?);", p2, gId);
  }

  static void conditionalInsert(Game g) {
    int gId = g.getId();
    int p1 = g.getActivePlayerId();
    int p2 = g.getOpposingPlayerId(p1);
    p1 = p1 > p2 ? p1 ^ p2 ^ (p2 = p1) : p1; // swap values if p1 > p2

    try (ResultSet rs = Db.query("select id from in_progress where id = ?;",
        gId)) {
      String serialG = g.serialize();
      if (!rs.next()) {
        System.out.println("I'm inserting");
        Db.update("insert into in_progress values(?, ?, ?, ?);", g.getId(), p1,
            p2, serialG);
      } else {
        System.out.println("I'm updating");
        Db.update("update in_progress set board = ? where player1 = ?;",
            serialG, p1);
      }
      System.out.println(String
          .format("Stashed game %d with players %d and %d in db", gId, p1, p2));
    } catch (IOException | SQLException | NullPointerException e) {
      e.printStackTrace();
    }
  }
}
