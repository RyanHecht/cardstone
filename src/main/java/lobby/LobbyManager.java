package lobby;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import game.GameManager;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import server.LobbyListWebSocket;
import server.LobbyWebSocket;

/**
 * In charge of managing Lobbies, the staging area for Games.
 * @author ryan
 *
 */
public class LobbyManager {
  private static Map<String, Lobby> lobbies = new ConcurrentHashMap<>();
  private static Gson gson = new Gson();

  /**
   * Creates a new Lobby.
   */
  public static Lobby addLobby(String name, boolean priv, String password,
      int hostUId) throws IllegalArgumentException {
    name = name.replace("<", "").replace(">", "");
    if (lobbies.containsKey(name)) {
      throw new IllegalArgumentException(
          "Lobby with name " + name + " already exists");
    } else if (priv && password.length() < 1) {
      throw new IllegalArgumentException(
          "Private lobbies must have non-empty passwords");
    } else if (LobbyManager.playerIsInLobby(hostUId)) {
      throw new IllegalArgumentException(
          "You're already in a lobby!");

    } else if (GameManager.playerIsInGame(hostUId)) {
      throw new IllegalArgumentException(
          "You're currently in a game, and cannot create a lobby.");
    } else {
      System.out.println("adding2: ");

      Lobby lobby = lobbies.put(name, new Lobby(name, priv, password, hostUId));
      System.out.println("Made lobby: " + lobby);
      LobbyListWebSocket.update();
      return lobby;
    }
  }

  /**
   * Cancels an existing Lobby.
   * @param name the name of the lobby to cancel.
   */
  public static void cancelLobby(String name) {
    lobbies.remove(name);
    LobbyListWebSocket.update();
  }

  /**
   * Get all lobbies that are currently open.
   * @return A Collection containing all lobbies.
   */
  public static Collection<Lobby> getAllLobbies() {
    for (String name : lobbies.keySet()) {
      System.out.println(name);
    }
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
      if (LobbyManager.playerIsInLobby(playerId)) {
        throw new IllegalArgumentException("You're already in a lobby!");
      } else if (GameManager.playerIsInGame(playerId)) {
        throw new IllegalArgumentException(
            "You're currently in a game, and cannot join a lobby.");
      }
      System.out.println(String.format("PID: %d, Name: %s, Pw: %s", playerId,
          lobbyName, password));
      Lobby l = lobbies.get(lobbyName);

      if (l != null) {
        lobbies.get(lobbyName).join(playerId, password);
        LobbyWebSocket.sendOpponentEnteredLobby(l.getOtherPlayer(playerId),
            playerId);

        for (Integer i : l.getAllSpectators()) {
          LobbyWebSocket.sendOpponentEnteredLobby(i, playerId);
        }
      } else {
        throw new IllegalArgumentException(
            "Lobby no longer exists. Pick another!");
      }

    } catch (IllegalArgumentException x) {
      throw x;
    }

  }

  public static void spectatorJoinLobby(int playerId, String lobbyName,
      String password)
          throws IllegalArgumentException {
    if (LobbyManager.playerIsInLobby(playerId)) {
      throw new IllegalArgumentException(
          "You're already in a lobby!");

    } else if (GameManager.playerIsInGame(playerId)) {
      throw new IllegalArgumentException(
          "You're currently in a game, and cannot create a lobby.");
    }
    Lobby l = lobbies.get(lobbyName);
    if (l != null) {
      try {
        l.joinSpectator(playerId, password);
      } catch (IllegalArgumentException x) {
        throw x;
      }

    }
  }

  public static void spectatorUpdate(int spectatorId, int spectateeId) {
    Lobby l = getLobbyByPlayerId(spectateeId);

    if (l != null) {
      System.out.println(
          "updating spectator " + spectatorId + " to spectatee " + spectateeId);
      l.changeSpectator(spectatorId, spectateeId);
    } else {
      System.out.println("couldn't find game for " + spectateeId);
    }
  }

  /**
   * Have the player of id playerId leave their lobby.
   * @param playerId The player Id.
   */
  public static void playerLeftLobby(int playerId) {
    Lobby l = getLobbyByPlayerId(playerId);
    if (l != null) {
      Integer other = l.getOtherPlayer(playerId);
      List<Integer> spectators = l.getAllSpectators();

      l.leave(playerId);

      if (l.isHost(playerId)) {
        // If host, cancel the lobby, alert other player and all spectators.
        if (!l.isStarting()) {
          if (other != null) {
            LobbyWebSocket.sendLobbyCancelled(other);
          }

          for (Integer i : spectators) {
            LobbyWebSocket.sendLobbyCancelled(i);
          }
        }
      } else {
        LobbyWebSocket.sendOpponentLeftLobby(other);

        for (Integer i : spectators) {
          LobbyWebSocket.sendOpponentLeftLobby(i);
        }
      }

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
      System.out.println(message.get("cards").toString());

      JsonArray cards = gson.fromJson(message.get("cards").getAsString(),
          JsonArray.class);
      List<String> cardList = new ArrayList<>();

      for (JsonElement card : cards) {
        cardList.add(card.getAsString());
        System.out.print(card.getAsString() + ", ");
      }
      System.out.print("\n");
      System.out.println("I gotta deck YAYAA");
      System.out.println(Arrays.toString(cardList.toArray()));

      lobby.setDeck(uId, cardList);
      LobbyWebSocket.sendOpponentSetDeck(lobby.getOtherPlayer(uId));
    }
  }

  public static void receivePlayerChat(int playerId, JsonObject message) {
    String chat = message.get("message").getAsString();
    Lobby lobby = getLobbyByPlayerId(playerId);
    if (lobby != null) {
      int idToSend = lobby.getOtherPlayer(playerId);
      try {
        LobbyWebSocket.sendChatMessage(idToSend, chat);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  /**
   * Handle websocket message of type START_GAME_REQUEST.
   * @param uId userId of person taking action.
   * @param message Message that was sent.
   */
  public static void handleStartGameRequest(int uId, JsonObject message) {
    Lobby lobby = getLobbyByPlayerId(uId);
    if (lobby != null) {
      int oppId = lobby.getOtherPlayer(uId);
      try {
        System.out.println("Beginning game...");
        lobby.beginGame();
        System.out.println("Sending messages...");
        for (Integer i : lobby.getAllSpectators()) {
          LobbyWebSocket.sendGameIsStarting(i, oppId);
        }

        LobbyWebSocket.sendGameIsStarting(oppId, uId);
        LobbyWebSocket.sendGameIsStarting(uId, oppId);

      } catch (IllegalArgumentException x) {
        try {
          LobbyWebSocket.sendChatMessage(uId,
              "ERROR: Could not begin game! \n" + x.getMessage());
        } catch (IOException e) {
          e.printStackTrace();
        }
      }

    }
  }

}
