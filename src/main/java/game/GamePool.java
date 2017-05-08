package game;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.ExecutionException;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.CacheLoader.InvalidCacheLoadException;

import gui.Db;

import com.google.common.cache.LoadingCache;
import com.google.common.cache.RemovalListener;
import com.google.common.cache.RemovalNotification;

/**
 * Represents pool of all games that are going on.
 * @author ryan an willayyy
 */
public class GamePool {
  private static final int POOL_SIZE = 1;
  private static LoadingCache<Integer, Game> playersToGames = CacheBuilder
      .newBuilder()
      .maximumSize(POOL_SIZE)
      .removalListener(new RemovalListener<Integer, Game>() {
        @Override
        public void onRemoval(RemovalNotification<Integer, Game> removal) {
          if (removal.wasEvicted()) {
            int id = removal.getKey();
            Game g = removal.getValue();
            System.out.println(String.format("Game %d for player %d was evicted!",
                    g.getId(), id));
            try {
              GameManager.conditionalInsert(g);
            } catch (IllegalStateException e) {
              System.out.println(String.format("Game %d for player %d was evicted!", id,
                    g.getId()));
            }
          }
        }
      }).build(new CacheLoader<Integer, Game>() {
        @Override
        public Game load(Integer key) {
          System.out.println("Trying to load game for user " + key);
          String gameFinder = "select board from in_progress where "
              + "player1 = ? or player2 = ?;";
          try (ResultSet rs = Db.query(gameFinder, key, key)) {
            if (!rs.next()) {
              System.out
                  .println("Could not load game from database for user " + key);
              return null;
            }
            Game g = Game.deserialize(rs.getString(1));
            
            System.out
                .println(String.format(
                    "Successfully retrieved game id %d for user %d", g.getId(),
                    key));
            return g;
          } catch (SQLException | NullPointerException | ClassNotFoundException
              | IOException e) {
            e.printStackTrace();
            return null;
          }
        }
      });

  public GamePool() {

  }

  // Returns true if game added, false otherwise
  // why do you need 2 user ids... aren't they already in the game object?
  public boolean updateGame(Game game) {
    // want to use this method to overwrite board states as well
    // so only return false if player is in game already with different id
    int u1 = game.getActivePlayerId();
    int u2 = game.getOpposingPlayerId(u1);
    int gId = game.getId();

    int g1;
    int g2;

    g1 = getGameByPlayerId(u1) == null ? gId
        : getGameByPlayerId(u1).getId();
    g2 = getGameByPlayerId(u1) == null ? gId
        : getGameByPlayerId(u1).getId();

    if (g1 != gId || g2 != gId) {
      System.out.println("already in game!");
      return false;
    } else {
      playersToGames.put(u1, game);
      playersToGames.put(u2, game);
      System.out.println(u1 + " and " + u2 + " are in a game.");
      return true;
    }
  }

  public void removeGame(int pid) {
    playersToGames.invalidate(pid);
  }

  public Game getGameByPlayerId(int id) {
    try {
      return playersToGames.get(id);
    } catch (ExecutionException | InvalidCacheLoadException e) {
      return null;
    }
  }
}
