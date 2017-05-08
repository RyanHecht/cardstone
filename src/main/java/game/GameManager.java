package game;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import gui.Db;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import server.CommsWebSocket;

/**
 * Class to keep track of all games currently running and handle playing them.
 * Can handle an arbitrary amount of games at once. Also stashes replay info.
 *
 * @author Raghu and Willayyy
 */
public class GameManager {
  private static final Gson GSON = new Gson();
  private static GamePool games = new GamePool();
  private static Map<Integer, Integer> gamesToEventNums = new ConcurrentHashMap<>();
  private static Map<Integer, JsonArray> gamesToAnimations = new ConcurrentHashMap<>();

  public static void addGame(Game game) {
    if (games.updateGame(game)) {

      int gId = game.getId();
      System.out.println(String.format("Trying to add game %d", gId));
      try {
        conditionalInsert(game);
        gamesToEventNums.put(gId, 1);
        gamesToAnimations.put(gId, new JsonArray());
      } catch (IllegalStateException e) {
        System.out.println(String.format("Error putting game %d in db", gId));
        e.printStackTrace();
      }
    }
  }

  // remove games when they're complete.
  public static void endGame(GameStats ended) {
    Game g = ended.getGame();
    int gId = g.getId();
    int winner = ended.getWinnerId();
    int turns = ended.getNumTurns();

    int firstUser = g.getActivePlayerId();
    int secondUser = g.getOpposingPlayerId(firstUser);
    System.out
        .println(String.format("Ending game %d with winner %d", gId, winner));
    registerFinishedGame(gId, firstUser, secondUser, winner, turns);

    games.removeGame(firstUser);
    games.removeGame(secondUser);
    gamesToEventNums.remove(gId);
  }

  public static void endGameExplicitWinners(GameStats ended, int winner) {
    Game g = ended.getGame();
    int gId = g.getId();
    // int winner = ended.getWinnerId();
    int turns = ended.getNumTurns();

    int firstUser = g.getActivePlayerId();
    int secondUser = g.getOpposingPlayerId(firstUser);
    System.out
        .println(String.format("Ending game %d with winner %d", gId, winner));
    registerFinishedGame(gId, firstUser, secondUser, winner, turns);

    games.removeGame(firstUser);
    games.removeGame(secondUser);
    gamesToEventNums.remove(gId);
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
      if (game instanceof DemoGame) {
        CommsWebSocket.sendTextMessage(uId, ((DemoGame) game).getMessage());

      }
      CommsWebSocket.sendWholeBoardSate(game, uId);
      CommsWebSocket.sendTurnStart(uId, game.isActivePlayer(uId));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Caches board states and inserts their JSON into the Db for replay purposes.
   * 
   * @param g the game
   */
  public static void pushToDb(Game g) {
    // add game to cache
    if (games.updateGame(g)) {
      String eventInsert = "insert into game_event values(?, ?, ?, ?);";
      int gId = g.getId();
      int eventNum = gamesToEventNums.get(gId);
      // insert JSON board state and animations into Db for replay purposes
      // and increment the event number
      try {
        System.out.println(
            "Trying to insert event num " + eventNum + " for game " + gId);
        JsonArray anims = gamesToAnimations.get(gId);

        Db.update(eventInsert, gId, eventNum, g.jsonifySelf(), anims);
        gamesToEventNums.put(gId, ++eventNum);
        gamesToAnimations.replace(gId, new JsonArray());
        System.out.println("Event num now " + gamesToEventNums.get(gId));
      } catch (SQLException e) {
        System.out.println(String.format("Error pushing game %d event %d to db",
            g.getId(), gamesToEventNums.get(g.getId())));
      } catch (NullPointerException e) {
        System.out.println(
            "Game " + g.getId() + " does not exist or has been completed.");
      }
    }
  }

  /**
   * Returns the board state for a game event as a JsonObject.
   *
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
    System.out.println("About to get starting id");
    try (ResultSet finished = Db.query("select max(id) from finished_game;")) {
      ret = finished.getInt(1);
      System.out.println("Got max " + ret + " from finished_game");
      try (ResultSet in_prog = Db.query("select max(id) from in_progress;")) {
        // return largest id of 2 tables
        System.out
            .println("Got max " + in_prog.getInt(1) + " from in_progress");
        ret = Math.max(ret, in_prog.getInt(1));
        System.out.println("Will return " + ret);
      }
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
      int turns) {
    try {
      System.out.println(String
          .format("Stashing game %d with players %d and %d", gId, p1, p2));
      String timestamp = DateFormat.getInstance().format(new Date());
      Db.update("delete from in_progress where id = ?;", gId);

      System.out.println("Am gonna insert with winner " + winner);
      Db.update("insert into finished_game values (?, ?, ?, ?, ?, ?);", gId,
          winner, p1, p2, turns, timestamp);
    } catch (NullPointerException | SQLException e) {
      System.out.println(
          String.format("Game %d with players %d and %d already in db",
              gId, p1, p2));
    }
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
      System.out.println(String.format(
          "Couldn't insert game %d with players %d and %d into db", gId, p1,
          p2));
      e.printStackTrace();
    }
  }
}
