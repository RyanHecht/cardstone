package lobby;

import cardgamelibrary.Jsonifiable;
import com.google.gson.JsonObject;
import game.Game;
import game.GameManager;
import java.util.ArrayList;
import java.util.List;
import server.CommsWebSocket;

/**
 * Represents a Lobby, a waiting room before a game is begun.
 * @author ryan
 *
 */
public class Lobby implements Jsonifiable {
  private final String name;
  private final boolean priv;
  private final String password;
  private final Integer uId1;
  private Integer uId2;
  private List<Integer> uId1Spectators;
  private List<Integer> uId2Spectators;
  private List<String> p1deck;
  private List<String> p2deck;

  /**
   * Construct a new lobby.
   * @param name The name this lobby should have.
   * @param priv Whether this lobby should be private.
   * @param password The password (empty if not private).
   * @param hostUId The id of the host player.
   */
  public Lobby(String name, boolean priv, String password, int hostUId) {
    this.name = name;
    this.priv = priv;
    this.password = password;
    this.uId1 = hostUId;
    uId1Spectators = new ArrayList<>();
    uId2Spectators = new ArrayList<>();
  }

  /**
   * Construct a non-private lobby.
   * @param name The name this lobby should have.
   * @param hostUId The id of the host player.
   */
  public Lobby(String name, int hostUId) {
    this.name = name;
    priv = false;
    password = null;
    this.uId1 = hostUId;
  }

  /**
   * Query whether this lobby is private (has a password).
   * @return True if it is private, false otherwise.
   */
  public boolean isPrivate() {
    return priv;
  }

  /**
   * Get the password of the lobby.
   * @return The password of the lobby, "" if not private.
   */
  public String getPassword() {
    return password;
  }

  public String getName() {
    return name;
  }

  /**
   * Get whether this lobby is full (has two players).
   * @return True if neither player slot is null, false otherwise.
   */
  public boolean isFull() {
    return (uId1 != null) && (uId2 != null);
  }

  /**
   * Get whether both players have their decks set.
   * @return True if decks have been initialized, false otherwise.
   */
  public boolean decksSet() {
    return (p1deck != null) && (p2deck != null);
  }

  public boolean hostDeckSet() {
    return p1deck != null;
  }

  /**
   * Get how many people are in the lobby.
   * @return The number of players in the lobby.
   */
  public int getCount() {
    if (isFull()) {
      return 2;
    } else {
      return 1;
    }
  }

  /**
   * Attempt to have a player join this lobby.
   * @param uId The id of the user to join.
   * @param password their proposed password ("" for non-private).
   * @throws IllegalArgumentException thrown if there's an issue joining.
   */
  public void join(int uId, String pw) throws IllegalArgumentException {
    if (priv) {
      if (!(this.password.equals(pw))) {
        throw new IllegalArgumentException("Incorrect password.");
      }
    }

    if (!isFull()) {
      uId2 = uId;
    } else {
      throw new IllegalArgumentException("Cannot join full room.");
    }
  }

  public void joinSpectator(int spectatorId, String pw)
      throws IllegalArgumentException {
    if (priv) {
      if (!(this.password.equals(pw))) {
        throw new IllegalArgumentException("Incorrect password.");
      }
    }

    uId1Spectators.add(spectatorId);
  }

  public void changeSpectator(int spectatorId, int spectateeId) {
    if (spectateeId == uId1) {
      uId1Spectators.add(spectatorId);
    } else if (spectateeId == uId2) {
      uId2Spectators.add(spectatorId);
    }
  }

  /**
   * Have a player leave the lobby
   * @param uId The uID of the player to leave.
   */
  public void leave(int uId) {
    if (uId == uId1) {
      LobbyManager.cancelLobby(this.name);
    } else if (uId == uId2) {
      this.uId2 = null;
      // System.out.println("handled " + uId + " leaving");
    }
  }

  /**
   * Begin the game. Will only start if lobby is full and both decks are set.
   */
  public void beginGame() {
    if (isFull() && decksSet()) {
      System.out.println("making game...");
      Game game = new Game(p1deck, p2deck, uId1, uId2);
      System.out.println("game made.");
      GameManager.addGame(uId1, uId2, game);
      CommsWebSocket.setSpectators(uId1, uId1Spectators);
      CommsWebSocket.setSpectators(uId2, uId2Spectators);
    } else {
      if (!isFull()) {
        throw new IllegalArgumentException("Lobby not full!");
      } else if (!decksSet()) {
        throw new IllegalArgumentException("Decks not set!");
      }
    }
  }

  public Integer getOtherPlayer(int uId) {
    System.out.println("Other player request from user " + uId);
    System.out
        .println("In lobby " + name + " with pw " + password + " and users "
            + uId1 + ", " + uId2);
    Integer ret = null;
    if (uId == uId1) {
      ret = uId2;
    } else if (uId == uId2) {
      ret = uId1;
    }

    return (ret == null) ? -1 : ret;
  }

  /**
   * Get whether a specified player is in this lobby.
   * @param uId The uId to check
   * @return True if the player is in the lobby, false otherwise.
   */
  public boolean containsPlayer(Integer uId) {
    return uId1 == uId || uId2 == uId;
  }

  public boolean isHost(int uId) {
    return uId1 == uId;
  }

  /**
   * Set the specified user's deck.
   * @param uId The user id to set the deck of.
   * @param deck The deck to set.
   */
  public void setDeck(int uId, List<String> deck) {
    if (uId == uId1) {
      p1deck = deck;
    } else if (uId == uId2) {
      p2deck = deck;
    }
  }

  public List<Integer> getSpectators(int uId) {
    if (uId == uId1) {
      return uId1Spectators;
    } else if (uId == uId2) {
      return uId2Spectators;
    } else {
      return null;
    }
  }

  public List<Integer> getAllSpectators() {
    List<Integer> all = new ArrayList<>();
    all.addAll(uId1Spectators);
    all.addAll(uId2Spectators);
    return all;
  }

  @Override
  public JsonObject jsonifySelf() {
    JsonObject obj = new JsonObject();
    obj.addProperty("name", this.name);
    obj.addProperty("full", getCount() > 1);
    obj.addProperty("private", priv);
    obj.addProperty("host", uId1);
    return obj;
  }

  @Override
  public JsonObject jsonifySelfChanged() {
    throw new UnsupportedOperationException();
  }

}
