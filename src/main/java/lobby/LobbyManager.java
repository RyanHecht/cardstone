package lobby;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import server.LobbyWebSocket;

/**
 * In charge of managing Lobbies, the staging area for Games.
 * @author ryan
 *
 */
public class LobbyManager {
  private static Map<String, Lobby> lobbies = new ConcurrentHashMap<>();

  /**
   * Creates a new Lobby.
   */
  public static Lobby addLobby(String name, boolean priv, String password,
      int hostUId) throws IllegalArgumentException {
    if (lobbies.containsKey(name)) {
      throw new IllegalArgumentException("Lobby with name " + name + " already exists");
    } else if (priv && password.length() < 1) {
      throw new IllegalArgumentException("Private lobbies must have non-empty passwords");
    } else {
      Lobby lobby = lobbies.put(name, new Lobby(name, priv, password, hostUId));
      System.out.println("Made lobby: " + lobby);
      return lobby;
    }
  }

  /**
   * Cancels an existing Lobby.
   * @param name the name of the lobby to cancel.
   */
  public static void cancelLobby(String name) {
    lobbies.remove(name);
  }

  /**
   * Get all lobbies that are currently open.
   * @return A Collection containing all lobbies.
   */
  public static Collection<Lobby> getAllLobbies() {
    return lobbies.values();
  }

  /**
   * Check whether a player is in a Lobby.
   * @param playerId Id of player to check.
   * @return True if player of id is in a lobby, false otherwise.
   */
  public static boolean playerIsInLobby(int playerId) {
    for (Lobby l : lobbies.values()) {
      if (l.containsPlayer(playerId)) {
        return true;
      }
    }
    return false;
  }

  /**
   * Attempt to have a player of a specified Id join a lobby.
   * @param playerId The player Id.
   * @param lobbyName The name of the lobby to add them to.
   * @param password the password of the lobby (empty string for non-private).
   * @throws IllegalArgumentException Thrown if player could not join the lobby
   *           (it's full, incorrect password, etc).
   */
  public static void playerJoinLobby(int playerId, String lobbyName,
      String password)
      throws IllegalArgumentException {
    try {
      System.out.println(String.format("PID: %d, Name: %s, Pw: %s", playerId, lobbyName, password));
      lobbies.get(lobbyName).join(playerId, password);
    } catch (IllegalArgumentException x) {
      throw x;
    }

  }

  /**
   * Get a Lobby from a player's id.
   * @param playerId Id of player to get lobby of.
   * @return The Lobby the player is in (null if not in lobby).
   */
  public static Lobby getLobbyByPlayerId(int playerId) {
    for (Lobby l : lobbies.values()) {
      if (l.containsPlayer(playerId)) {
        return l;
      }
    }
    return null;
  }

  /**
   * Get a Lobby by its name.
   * @param lobbyName the name to get the lobby of.
   * @return The lobby represented by the name (null if doesn't exist).
   */
  public static Lobby getLobbyByName(String lobbyName) {
    if (lobbies.containsKey(lobbyName)) {
      return lobbies.get(lobbyName);
    } else {
      return null;
    }
  }

  /**
   * Handle websocket message of type SELF_SET_DECK.
   * @param uId userId of person joining lobby.
   * @param message Message that was sent.
   */
  public static void handleSelfSetDeck(int uId, JsonObject message) {
    Lobby lobby = getLobbyByPlayerId(uId);
    if (lobby != null) {
      JsonObject deck = message.get("deck").getAsJsonObject();
      String deckName = deck.get("name").getAsString();
      JsonArray cards = deck.get("cards").getAsJsonArray();
      List<String> cardList = new ArrayList<>();

      for (JsonElement card : cards) {
        cardList.add(card.getAsString());
        System.out.print(card.getAsString() + ", ");
      }
      System.out.print("\n");
      lobby.setDeck(uId, cardList);
      LobbyWebSocket.sendOppenentSetDeck(lobby.getOtherPlayer(uId), deckName);
    }
  }

  /**
   * Handle websocket message of type START_GAME_REQUEST.
   * @param uId userId of person taking action.
   * @param message Message that was sent.
   */
  public static void handleStartGameRequest(int uId, JsonObject message) {

  }

}
