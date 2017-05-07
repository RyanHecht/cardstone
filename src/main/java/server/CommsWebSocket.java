package server;

import cardgamelibrary.Board;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import game.Game;
import game.GameManager;
import game.GameStats;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

/**
 * Spark WebSocket for sending data between server and client
 *
 * @author Ryan
 *
 */
@WebSocket
public class CommsWebSocket {

  private static final Gson GSON = new Gson();
  private static final Map<Session, Integer> sessions = new ConcurrentHashMap<>();
  private static final Map<Integer, Session> idToSessions = new ConcurrentHashMap<>();
  private static final Map<Integer, List<Integer>> spectators = new ConcurrentHashMap<>();

  @OnWebSocketConnect
  public void connected(Session session) throws IOException {
    // On a new connection, we should send them an ID_REQUEST.

  }

  @OnWebSocketClose
  public void closed(Session session, int statusCode, String reason) {
    int id = sessions.get(session);

    Game game = GameManager.getGameByPlayerId(id);

    if (game != null) {
      int winner = game.getOpposingPlayerId(id);
      GameStats stats = new GameStats(game, id);
      GameManager.endGame(stats);
      System.out.println("Game ended because " + id + " left.");
      try {
        CommsWebSocket.sendGameEnd(winner, "Opponent left game.");
        CommsWebSocket.sendGameEndToSpectators(id,
            "The player you were spectating left the game.");
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    sessions.remove(session);
    idToSessions.remove(Integer.valueOf(id));

    if (spectators.get(id) != null) {
      spectators.remove(id);
    }

    System.out.println("disconnected " + id);
  }

  @OnWebSocketMessage
  public void message(Session session, String message) throws IOException {
    // Get the object received, the message type, and the payload
    System.out.println("Got a message!" + "\n" + message);

    // Ignore heartbeats
    if (message.equals("lubdub")) {
      return;
    }

    JsonObject received = GSON.fromJson(message, JsonObject.class);
    int type = received.get("type").getAsInt();
    JsonObject payload = received.get("payload").getAsJsonObject();
    // These types of messages can only be operated on fully added sessions
    try {

      if (sessions.containsKey(session)) {

        int id = sessions.get(session);

        if (type == MessageTypeEnum.UNDERSTOOD_BOARD_STATE.ordinal()) {
          GameManager.receiveUnderstoodBoardState(id, payload);
        } else if (type == MessageTypeEnum.TARGETED_CARD.ordinal()) {
          GameManager.receiveTargetedCard(id, payload);
        } else if (type == MessageTypeEnum.TARGETED_PLAYER.ordinal()) {
          GameManager.receiveTargetedPlayer(id, payload);
        } else if (type == MessageTypeEnum.ATTEMPTED_TO_PLAY.ordinal()) {
          GameManager.receiveAttemptedToPlay(id, payload);
        } else if (type == MessageTypeEnum.CHOOSE_RESPONSE.ordinal()) {
          GameManager.receiveChooseResponse(id, payload);
        } else if (type == MessageTypeEnum.TARGET_RESPONSE.ordinal()) {

        } else if (type == MessageTypeEnum.TURN_END.ordinal()) {
          GameManager.receiveTurnEnd(id);
        } else if (type == MessageTypeEnum.PLAYER_SEND_CHAT.ordinal()) {
          GameManager.receivePlayerChat(id, payload);
        }
      } else {
        // ID_RESPONSE is the only thing that the client should send it if it
        // isn't in the map yet.
        if (type == MessageTypeEnum.ID_RESPONSE.ordinal()) {
          // Let's get the Id and put it in the map!
          int id = payload.get("id").getAsInt();

          if (idToSessions.containsKey(id)) {
            System.out.println(id + " is already connected. sending message.");
            JsonObject obj = new JsonObject();
            obj.addProperty("message",
                "You already have the game window open!");
            sendMessageToSession(session, MessageTypeEnum.GAME_END, obj);
          } else {
            sessions.put(session, id);
            idToSessions.put(id, session);

            if (isSpectator(id)) {
              System.out.println(id + " is a spectator!!");
              int spectatee = getSpectatee(id);
              CommsWebSocket.sendIsSpectator(id, spectatee);
              CommsWebSocket.sendWholeBoardSate(
                  GameManager.getGameByPlayerId(spectatee), id);
              CommsWebSocket.sendTurnStart(id,
                  GameManager.getGameByPlayerId(spectatee)
                      .isActivePlayer(spectatee));
            } else {
              GameManager.playerIsReady(id);
            }
          }
        }
      }
    } catch (Exception ex) {
      System.out.println("ERROR in message handling");
      ex.printStackTrace();

    }

  }

  public static boolean isSpectator(Integer userId) {
    for (List<Integer> specList : spectators.values()) {
      if (specList.contains(userId)) {
        return true;
      }
    }
    return false;
  }

  public static void removeIfSpectator(Integer userId) {
    Iterator<List<Integer>> it = spectators.values().iterator();
    while (it.hasNext()) {
      List<Integer> specList = it.next();

      if (specList.contains(userId)) {
        specList.remove(userId);
        return;
      }
    }

  }

  public static int getSpectatee(Integer userId) {
    for (Integer spectatee : spectators.keySet()) {
      if (spectators.get(spectatee).contains(userId)) {
        return spectatee;
      }
    }
    return -999;
  }

  /**
   * Update the user given by userId on changes in the board state.
   *
   * @param toSend
   * @param userId
   * @throws IOException
   */
  public static void sendChangedBoardSate(Board toSend, int userId)
      throws IOException {
    if (idToSessions.containsKey(userId)) {
      Session session = idToSessions.get(Integer.valueOf(userId));
      JsonObject obj = new JsonObject();
      JsonObject payload = toSend.jsonifySelfChanged();
      obj.addProperty("type", MessageTypeEnum.BOARD_STATE.ordinal());
      obj.add("payload", payload);

      session.getRemote().sendString(GSON.toJson(obj));
    }
  }

  public static void sendIsSpectator(int userId, int watching)
      throws IOException {
    if (idToSessions.containsKey(userId)) {
      Session session = idToSessions.get(userId);
      JsonObject obj = new JsonObject();
      JsonObject payload = new JsonObject();
      payload.addProperty("watching", watching);
      obj.addProperty("type", MessageTypeEnum.SET_SPECTATOR.ordinal());
      obj.add("payload", payload);

      session.getRemote().sendString(GSON.toJson(obj));
    }
  }

  /**
   * Send the entire game state to the user given by userId.
   *
   * @param toSend
   * @param userId
   * @throws IOException
   */
  public static void sendWholeBoardSate(Game toSend, int userId)
      throws IOException {
    JsonObject payload = toSend.jsonifySelf();
    System.out.println("Sending board: " + payload.toString());
    sendMessage(userId, MessageTypeEnum.BOARD_STATE, payload);
  }

  /**
   * Send an animation the front end knows about.
   * @param userId Id of recipient.
   * @param message animation message.
   * @throws IOException thrown by websocket.
   */
  public static void sendAnimation(int userId, JsonObject message)
      throws IOException {
    System.out.println("Sending animation " + message.toString());
    sendMessage(userId, MessageTypeEnum.ANIMATION, message);
  }

  /**
   * Send an explicit animation that the server doesn't know about.
   * @param userId Id of recipient.
   * @param message Message containing animation data.
   * @throws IOException thrown by websocket.
   */
  public static void sendExplicitAnimation(int userId, JsonObject message)
      throws IOException {
    System.out.println("Sending animation " + message.toString());
    sendMessage(userId, MessageTypeEnum.EXPLICIT_ANIMATION, message);
  }

  /**
   * Send a request by the server for the player to choose from a list of cards.
   * @param userId Id of recipient.
   * @param message Message containing options.
   * @throws IOException thrown by websocket.
   */
  public static void sendChooseRequest(int userId, JsonObject message)
      throws IOException {
    System.out.println("Sending choose request " + message.toString());
    sendMessage(userId, MessageTypeEnum.CHOOSE_REQUEST, message);
  }

  /**
   * Inform the user that their action was correct.
   * @param userId The id of the recipient user.
   * @throws IOException thrown by websocket.
   */
  public static void sendActionOk(int userId) throws IOException {
    JsonObject obj = new JsonObject();
    obj.addProperty("status", "ok");
    System.out.println("Sending action ok");
    sendMessage(userId, MessageTypeEnum.ACTION_OK, obj);
  }

  /**
   * Inform the user that their action was incorrect.
   * @param uderId The id of the recipient user.
   * @param message The message of why the action was bad.
   * @throws IOException thrown by websocket.
   */
  public static void sendActionBad(int userId, String message)
      throws IOException {
    JsonObject obj = new JsonObject();
    obj.addProperty("message", message);
    System.out.println("Sending action bad " + message);
    sendMessage(userId, MessageTypeEnum.ACTION_BAD, obj);
  }

  public static void sendTurnStart(int userId, boolean userIdsTurn)
      throws IOException {
    JsonObject obj = new JsonObject();
    obj.addProperty("isSelf", userIdsTurn);
    System.out
        .println("Sending turn start to " + userId + "...it's " + userIdsTurn);
    sendMessage(userId, MessageTypeEnum.TURN_START, obj);
  }

  /**
   * Send text message to be alerted to the user.
   * @param userId Id of recipient.
   * @param message Message to be alerted.
   * @throws IOException thrown by websocket.
   */
  public static void sendTextMessage(int userId, String message)
      throws IOException {
    JsonObject obj = new JsonObject();
    obj.addProperty("message", message);
    System.out.println("Sending message " + message);
    sendMessage(userId, MessageTypeEnum.TEXT_MESSAGE, obj);
  }

  /**
   * Send message to be displayed in the in-game chat.
   * @param userId Id of recipient.
   * @param message Message for chat.
   * @throws IOException thrown by websocket.
   */
  public static void sendChatMessage(int userId, String message)
      throws IOException {
    JsonObject obj = new JsonObject();
    obj.addProperty("message", message);
    System.out.println("Sending chat message: " + message);
    sendMessage(userId, MessageTypeEnum.RECEIVE_CHAT, obj);
  }

  /**
   * Alert the user given by userId (and his spectators) that the game has
   * ended.
   * @param userId Id of recipient.
   * @param message Message characterizing condition that made the game end.
   * @throws IOException thrown by websocket.
   */
  public static void sendGameEnd(int userId, String message)
      throws IOException {
    JsonObject obj = new JsonObject();
    obj.addProperty("message", message);
    System.out.println("Sending game end: " + message);
    sendMessage(userId, MessageTypeEnum.GAME_END, obj);
  }

  public static void sendGameEndToSpectators(int userId, String message)
      throws IOException {
    JsonObject payload = new JsonObject();
    payload.addProperty("message", message);
    System.out.println("Sending game end: " + message);

    if (spectators.get(userId) != null && spectators.get(userId).size() > 0) {

      for (Integer spectator : spectators.get(userId)) {
        System.out.println(userId + " has a spectator!");
        if (idToSessions.containsKey(spectator)) {
          System.out.println("The spectator has the page opened!");
          Session session = idToSessions.get(spectator);
          JsonObject obj = new JsonObject();
          obj.addProperty("type", MessageTypeEnum.GAME_END.ordinal());
          obj.add("payload", payload);

          session.getRemote().sendString(GSON.toJson(obj));
        }
      }
    }
  }

  public static void closeSession(int userId) {

  }

  public static void setSpectators(int spectateeId,
      List<Integer> spectatorList) {
    spectators.put(spectateeId, spectatorList);
    for (Integer i : spectatorList) {
      System.out.println("setting " + i + " as spectator to " + spectateeId);
    }
  }

  private static void sendMessage(int userId, MessageTypeEnum type,
      JsonObject payload) throws IOException {
    if (idToSessions.containsKey(userId)) {
      Session session = idToSessions.get(Integer.valueOf(userId));
      if (session == null) {
        System.out.println("session is null");
      }
      JsonObject obj = new JsonObject();
      obj.addProperty("type", type.ordinal());
      obj.add("payload", payload);

      session.getRemote().sendString(GSON.toJson(obj));
    }

    if (spectators.get(userId) != null && spectators.get(userId).size() > 0) {

      for (Integer spectator : spectators.get(userId)) {
        System.out.println(userId + " has a spectator!");
        if (idToSessions.containsKey(spectator)) {
          System.out.println("The spectator has the page opened!");
          Session session = idToSessions.get(spectator);
          JsonObject obj = new JsonObject();
          obj.addProperty("type", type.ordinal());
          obj.add("payload", payload);

          session.getRemote().sendString(GSON.toJson(obj));
        }
      }
    }

  }

  private static void sendMessageToSession(Session session,
      MessageTypeEnum type, JsonObject payload) throws IOException {
    JsonObject obj = new JsonObject();
    obj.addProperty("type", type.ordinal());
    obj.add("payload", payload);
    System.out.println("a message is being sent now.");
    session.getRemote().sendString(GSON.toJson(obj));
  }

}
