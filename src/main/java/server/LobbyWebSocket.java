package server;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lobby.Lobby;
import lobby.LobbyManager;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

@WebSocket
public class LobbyWebSocket {
  private static final Gson GSON = new Gson();
  private static final Map<Session, Integer> sessions = new ConcurrentHashMap<>();
  private static final Map<Integer, Session> idToSessions = new ConcurrentHashMap<>();

  @OnWebSocketConnect
  public void connected(Session session) throws IOException {
    // On a new connection, we should send them an ID_REQUEST.

  }

  @OnWebSocketClose
  public void closed(Session session, int statusCode, String reason) {
    int id = sessions.get(session);

    // TODO: Check if player is in game or in lobby, terminate game or update
    // lobby.
    System.out.println(id + " left");
    LobbyManager.playerLeftLobby(id);
    sessions.remove(session);
    idToSessions.remove(id);
    // System.out.println("took away " + id + "'s session.");
  }

  @OnWebSocketMessage
  public void message(Session session, String message) throws IOException {
    // Get the object received, the message type, and the payload
    System.out.println(message);
    JsonObject received = GSON.fromJson(message, JsonObject.class);
    int type = received.get("type").getAsInt();
    JsonObject payload = received.get("payload").getAsJsonObject();
    // These types of messages can only be operated on fully added sessions
    if (sessions.containsKey(session)) {

      int id = sessions.get(session);

      if (type == LobbyMessageTypeEnum.SELF_SET_DECK.ordinal()) {
        LobbyManager.handleSelfSetDeck(id, payload);
      } else if (type == LobbyMessageTypeEnum.START_GAME_REQUEST.ordinal()) {
        LobbyManager.handleStartGameRequest(id, payload);
      } else if (type == LobbyMessageTypeEnum.PLAYER_SEND_CHAT.ordinal()) {
        LobbyManager.receivePlayerChat(id, payload);
      } else if (type == LobbyMessageTypeEnum.SPECTATEE_UPDATE.ordinal()) {
        LobbyManager.spectatorUpdate(id, payload.get("id").getAsInt());
      }

    } else {
      // These are the message types that happen on connection.
      if (type == LobbyMessageTypeEnum.LOBBY_CONNECT.ordinal()) {
        // Let's get the Id and put it in the map!
        int id = payload.get("id").getAsInt();
        sessions.put(session, id);
        idToSessions.put(id, session);
        Lobby l = LobbyManager.getLobbyByPlayerId(id);

        if (l.hostDeckSet()) {
          LobbyWebSocket.sendOpponentSetDeck(id);
        } else {
          LobbyWebSocket.sendOpponentEnteredLobby(id,
              l.getOtherPlayer(id));
        }

      } else if (type == LobbyMessageTypeEnum.LOBBY_CREATE.ordinal()) {
        int id = payload.get("id").getAsInt();
        sessions.put(session, id);
        idToSessions.put(id, session);
      } else if (type == LobbyMessageTypeEnum.SPECTATOR_CONNECT.ordinal()) {
        int id = payload.get("id").getAsInt();
        sessions.put(session, id);
        idToSessions.put(id, session);
      }
    }

  }

  public static void closeSession(int userId) {

  }

  /**
   * Send message that Opponent has entered a lobby.
   * @param uId Id of recipient.
   * @param oppUId opponent's id.
   */
  public static void sendOpponentEnteredLobby(int uId, int oppUId) {
    JsonObject payload = new JsonObject();
    payload.addProperty("id", oppUId);
    try {
      sendMessage(uId, LobbyMessageTypeEnum.OPPONENT_ENTERED_LOBBY,
          payload);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Send message that Opponent has left a lobby.
   * @param uId Id of recipient.
   */
  public static void sendOpponentLeftLobby(int uId) {
    try {
      System.out.println("sending leaving to " + uId);
      sendMessage(uId, LobbyMessageTypeEnum.OPPONENT_LEFT_LOBBY,
          new JsonObject());
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Send message that opponent has set their deck.
   * @param uId Id of recipient.
   * @param deckName The name of the opponent's deck.
   */
  public static void sendOpponentSetDeck(int uId) {
    JsonObject payload = new JsonObject();
    payload.addProperty("id", uId);
    try {
      sendMessage(uId, LobbyMessageTypeEnum.OPPONENT_SET_DECK, payload);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Send message telling user the game is starting.
   * @param uId Id of recipient.
   * @param oppUId The opponent's Id.
   */
  public static void sendGameIsStarting(int uId, int oppUId) {
    JsonObject payload = new JsonObject();
    payload.addProperty("opponentId", oppUId);
    try {
      sendMessage(uId, LobbyMessageTypeEnum.GAME_IS_STARTING, payload);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Send message telling the user the lobby is cancelled.
   * @param uId Id of recipient.
   */
  public static void sendLobbyCancelled(int uId) {
    try {
      sendMessage(uId, LobbyMessageTypeEnum.LOBBY_CANCELLED,
          new JsonObject());
    } catch (IOException e) {
      e.printStackTrace();
    }
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
    sendMessage(userId, LobbyMessageTypeEnum.RECEIVE_CHAT, obj);
  }

  private static void sendMessage(int userId, LobbyMessageTypeEnum type,
      JsonObject payload) throws IOException {
    if (idToSessions.containsKey(userId)) {
      Session session = idToSessions.get(userId);
      JsonObject obj = new JsonObject();
      obj.addProperty("type", type.ordinal());
      obj.add("payload", payload);
      System.out.println("a lobby message is being sent now.");
      session.getRemote().sendString(GSON.toJson(obj));
    }
  }

  private static void sendMessageToSession(Session session,
      LobbyMessageTypeEnum type, JsonObject payload) throws IOException {
    JsonObject obj = new JsonObject();
    obj.addProperty("type", type.ordinal());
    obj.add("payload", payload);
    System.out.println("a lobby message is being sent now.");
    session.getRemote().sendString(GSON.toJson(obj));
  }

}
