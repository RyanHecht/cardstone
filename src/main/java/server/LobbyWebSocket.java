package server;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lobby.LobbyManager;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;

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

    sessions.remove(session);
    idToSessions.remove(id);
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
      }

    } else {
      // These are the message types that happen on connection.
      if (type == LobbyMessageTypeEnum.LOBBY_CONNECT.ordinal()) {
        // Let's get the Id and put it in the map!
        int id = payload.get("id").getAsInt();
        sessions.put(session, id);
        idToSessions.put(id, session);

      } else if (type == LobbyMessageTypeEnum.LOBBY_CREATE.ordinal()) {

      }
    }

  }

  public static void closeSession(int userId) {

  }

  public static void sendOppenentEnteredLobby(int uId, int oppUId) {
    try {
      sendMessage(uId, LobbyMessageTypeEnum.OPPONENT_ENTERED_LOBBY,
          new JsonObject());
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static void sendOppenentLeftLobby(int uId) {
    try {
      sendMessage(uId, LobbyMessageTypeEnum.OPPONENT_LEFT_LOBBY,
          new JsonObject());
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static void sendOppenentSetDeck(int uId, String deckName) {
    JsonObject payload = new JsonObject();
    payload.addProperty("name", deckName);
    try {
      sendMessage(uId, LobbyMessageTypeEnum.OPPONENT_SET_DECK, payload);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static void sendGameIsStarting(int uId, int oppUId) {
    JsonObject payload = new JsonObject();
    payload.addProperty("opponentId", oppUId);
    try {
      sendMessage(uId, LobbyMessageTypeEnum.GAME_IS_STARTING, payload);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static void sendLobbyCancelled(int uId) {
    try {
      sendMessage(uId, LobbyMessageTypeEnum.LOBBY_CANCELLED,
          new JsonObject());
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private static void sendMessage(int userId, LobbyMessageTypeEnum type,
      JsonObject payload) throws IOException {
    if (idToSessions.containsKey(userId)) {
      Session session = idToSessions.get(userId);
      JsonObject obj = new JsonObject();
      obj.addProperty("type", type.ordinal());
      obj.add("payload", payload);

      session.getRemote().sendString(GSON.toJson(obj));
    }
  }

}
