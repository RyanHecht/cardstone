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
      int p1 = game.getActivePlayerId();
      int p2 = game.getOpposingPlayerId(p1);
      if (p1 > p2) {
        p1 = p1 ^ p2 ^ (p2 = p1);
      }
      try {
        String serialG = game.serialize();
        Db.update("insert into in_progress values(?, ?, ?, ?);",
            game.getId(), p1, p2, serialG);
      } catch (SQLException | IOException e) {
        e.printStackTrace();
      }
      gamesToEventNums.put(game.getId(), 1);
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
        Db.update(eventInsert, gId, eventNum, g.jsonifySelf(),
            gamesToAnimations.get(gId));
        gamesToEventNums.put(gId, ++eventNum);
        gamesToAnimations.remove(gId);
        System.out.println("Event num now " + gamesToEventNums.get(gId));
      } catch (SQLException | NullPointerException e) {
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
    if (anims == null) {
      anims = new JsonArray();
      gamesToAnimations.put(gameId, anims);
    }
    anims.add(anim);
  }

  // Transitions game from in_progress to finished_game
  public static void registerFinishedGame(int gId, int p1, int p2, int winner,
      int turns) throws NullPointerException, SQLException {

    Db.update("delete from in_progress where id = ?;", gId);
    Db.update("insert into finished_game values(?, ?, ?);", gId, winner, turns);
    Db.update("insert into user_game values(?, ?);", p1, gId);
    Db.update("insert into user_game values(?, ?);", p2, gId);
  }
}
