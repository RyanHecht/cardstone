package server.websocket;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import cardgamelibrary.Board;
import server.MessageTypeEnum;

@WebSocket
public class CommsWebSocket {
  private static final Gson GSON = new Gson();
  private static final Map<Session, Integer> sessions = new ConcurrentHashMap<>();
  private static final Map<Integer, Session> idToSessions = new ConcurrentHashMap<>();

  @OnWebSocketConnect
  public void connected(Session session) throws IOException {
    // On a new connection, we should send them an ID_REQUEST.
    session.getRemote().sendString(GSON.toJson(getIdRequest()));
  }

  @OnWebSocketClose
  public void closed(Session session, int statusCode, String reason) {
    int id = sessions.get(session);

    // TODO: get player associated with this Id and terminate the game associated with that player.

    sessions.remove(session);
    idToSessions.remove(id);
  }

  @OnWebSocketMessage
  public void message(Session session, String message) throws IOException {
    // Get the object received, the message type, and the payload
    JsonObject received = GSON.fromJson(message, JsonObject.class);
    int type = received.get("type").getAsInt();
    JsonObject payload = received.get("payload").getAsJsonObject();

    // These types of messages can only be operated on fully added sessions
    if (sessions.containsKey(session)) {
      if (type == MessageTypeEnum.USER_INPUT.ordinal()) {

      }
    } else  {
      // ID_RESPONSE is the only thing that the client should send it if it isn't in the map yet.
      if (type == MessageTypeEnum.ID_RESPONSE.ordinal()) {
        // Let's get the Id and put it in the map!
        int id = payload.get("id").getAsInt();
        sessions.put(session, id);
        idToSessions.put(id, session);
      }
    }


  }


  public static void sendBoardSate(Board toSend, int userId) throws IOException {
    if (idToSessions.containsKey(userId)) {
      Session session = idToSessions.get(userId);
      JsonObject obj = new JsonObject();
      JsonObject payload = toSend.jsonifySelf();
      obj.addProperty("type", MessageTypeEnum.BOARD_STATE.ordinal());
      obj.add("payload", payload);

      session.getRemote().sendString(GSON.toJson(obj));
    }
  }

  private JsonObject getIdRequest() {
    JsonObject obj = new JsonObject();
    JsonObject payload=  new JsonObject();
    obj.addProperty("type", MessageTypeEnum.ID_REQUEST.ordinal());
    obj.add("payload", payload);

    return obj;
  }

}

