package server;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import java.io.IOException;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import lobby.Lobby;
import lobby.LobbyManager;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

@WebSocket
public class LobbyListWebSocket {
  private static final Queue<Session> sessions = new ConcurrentLinkedQueue<>();
  private static final Gson GSON = new Gson();

  @OnWebSocketConnect
  public void connected(Session session) {
    sessions.add(session);
  }

  @OnWebSocketClose
  public void disconnect(Session session, int statusCode, String reason) {
    sessions.remove(session);
  }

  public static void update() {
    JsonArray array = new JsonArray();
    for (Lobby l : LobbyManager.getAllLobbies()) {
      array.add(l.jsonifySelf());
    }

    for (Session session : sessions) {
      try {
        session.getRemote().sendString(GSON.toJson(array));
        // System.out.println("updated lobby list");
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

}