package game;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class GamePool {
  Map<Integer, Game> playerIdsToGames = new ConcurrentHashMap<>();

  public GamePool() {

  }

  public void addGame(int uId1, int uId2, Game game) {
    if (playerIdsToGames.containsKey(uId1)
        || playerIdsToGames.containsKey(uId2)) {

    } else {
      playerIdsToGames.put(uId1, game);
      playerIdsToGames.put(uId2, game);
    }
  }

  public Game getGameByPlayerId(int id) {
    return playerIdsToGames.get(id);
  }
}
