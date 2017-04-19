package server.websocket;

import cardgamelibrary.Board;
import cardgamelibrary.OrderedCardCollection;
import cardgamelibrary.Zone;
import cards.SkyWhaleCreature;
import cards.StubCreature;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import game.Player;
import game.PlayerType;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import server.MessageTypeEnum;

@WebSocket
public class CommsWebSocket {
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

    // TODO: get player associated with this Id and terminate the game
    // associated with that player.

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

      if (type == MessageTypeEnum.UNDERSTOOD_BOARD_STATE.ordinal()) {

      } else if (type == MessageTypeEnum.TARGETED_CARD.ordinal()) {

      } else if (type == MessageTypeEnum.TARGETED_PLAYER.ordinal()) {

      } else if (type == MessageTypeEnum.ATTEMPTED_TO_PLAY.ordinal()) {

      } else if (type == MessageTypeEnum.CHOOSE_RESPONSE.ordinal()) {

      } else if (type == MessageTypeEnum.TARGET_RESPONSE.ordinal()) {

      }
    } else {
      // ID_RESPONSE is the only thing that the client should send it if it
      // isn't in the map yet.
      if (type == MessageTypeEnum.ID_RESPONSE.ordinal()) {
        // Let's get the Id and put it in the map!
        int id = payload.get("id").getAsInt();
        sessions.put(session, id);
        idToSessions.put(id, session);
        CommsWebSocket.sendWholeBoardSate(testBoard(), id);
      }
    }

  }

  public static void sendChangedBoardSate(Board toSend, int userId)
      throws IOException {
    if (idToSessions.containsKey(userId)) {
      Session session = idToSessions.get(userId);
      JsonObject obj = new JsonObject();
      JsonObject payload = toSend.jsonifySelfChanged();
      obj.addProperty("type", MessageTypeEnum.BOARD_STATE.ordinal());
      obj.add("payload", payload);

      session.getRemote().sendString(GSON.toJson(obj));
    }
  }

  public static void sendWholeBoardSate(Board toSend, int userId)
      throws IOException {
    if (idToSessions.containsKey(userId)) {
      Session session = idToSessions.get(userId);
      JsonObject obj = new JsonObject();
      JsonObject payload = toSend.jsonifySelf();
      obj.addProperty("type", MessageTypeEnum.BOARD_STATE.ordinal());
      obj.add("payload", payload);

      session.getRemote().sendString(GSON.toJson(obj));
    }
  }

  public static void closeSession(int userId) {

  }

  private static Board testBoard() {
    Player pOne = new Player(30, PlayerType.PLAYER_ONE);
    Player pTwo = new Player(30, PlayerType.PLAYER_TWO);
    OrderedCardCollection deckOne = new OrderedCardCollection(Zone.DECK, pOne);
    OrderedCardCollection deckTwo = new OrderedCardCollection(Zone.DECK, pTwo);
    Board b1 = new Board(deckOne, deckTwo);
    StubCreature c1 = new StubCreature(pOne);
    StubCreature c2 = new StubCreature(pTwo);
    SkyWhaleCreature c3 = new SkyWhaleCreature(pOne);
    OrderedCardCollection playerOneCreatures = new OrderedCardCollection(
        Zone.CREATURE_BOARD, pOne);
    OrderedCardCollection playerTwoCreatures = new OrderedCardCollection(
        Zone.CREATURE_BOARD, pTwo);

    playerOneCreatures.add(c1);
    playerOneCreatures.add(c3);
    playerTwoCreatures.add(c2);

    b1.setOcc(playerOneCreatures);
    b1.setOcc(playerTwoCreatures);

    SkyWhaleCreature c4 = new SkyWhaleCreature(pTwo);
    OrderedCardCollection playerOneCreatures2 = new OrderedCardCollection(
        Zone.CREATURE_BOARD, pOne);
    OrderedCardCollection playerTwoCreatures2 = new OrderedCardCollection(
        Zone.CREATURE_BOARD, pTwo);

    playerOneCreatures2.add(c1);
    playerOneCreatures2.add(c3);
    playerTwoCreatures2.add(c2);
    playerTwoCreatures2.add(c4);
    playerOneCreatures2.add(c3);
    playerOneCreatures2.add(c3);
    b1.setOcc(playerOneCreatures2);
    b1.setOcc(playerTwoCreatures2);
    return b1;
  }

}
