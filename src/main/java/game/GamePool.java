package game;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class GamePool {
  Map<Integer, Game> playerIdsToGames = new ConcurrentHashMap<>();

  public GamePool() {

  }

  public void addGame() {
    // if (playerIdsToGames.containsKey(key))
  }

  public Game getGameByPlayerId(int id) {
    return playerIdsToGames.get(id);
  }
}
