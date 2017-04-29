package game;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Represents pool of all games that are going on.
 * @author ryan
 *
 */
public class GamePool {
  Map<Integer, Game> playerIdsToGames = new ConcurrentHashMap<>();

  public GamePool() {

  }

  public void addGame(int uId1, int uId2, Game game) {
    if (playerIdsToGames.containsKey(uId1)
        || playerIdsToGames.containsKey(uId2)) {
      System.out.println("already in game!");
    } else {
      playerIdsToGames.put(uId1, game);
      playerIdsToGames.put(uId2, game);
      System.out.println(uId1 + " and " + uId2 + " are in a game.");
    }
  }

  public Game getGameByPlayerId(int id) {
    return playerIdsToGames.get(id);
  }
}
