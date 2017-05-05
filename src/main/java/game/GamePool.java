package game;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.ExecutionException;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.CacheLoader.InvalidCacheLoadException;
import com.google.common.cache.LoadingCache;
import com.google.common.cache.RemovalListener;
import com.google.common.cache.RemovalNotification;

import logins.Db;

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
            System.out.println("Game with id " + id + " was evicted!");
            int otherId = g.getOpposingPlayerId(id);
            // ensure id is less than otherId for db consistency
            if (id > otherId) {
              id = id ^ otherId ^ (otherId = id); // swaps values
            }

            String dbCheck = "select id from in_progress where player1 = ?;";
            try (ResultSet rs = Db.query(dbCheck, id)) {
              String serialG = g.serialize();
              // if this game isn't in db, insert it; otherwise, update it
              if (!rs.next()) {
                Db.update("insert into in_progress values(null, ?, ?, ?);", id,
                    otherId, serialG);
              } else {
                Db.update("update in_progress set board = ? where player1 = ?;",
                    serialG, id);
              }
              System.out.println("Successfully stashed game " + id + " in db");
            } catch (IOException | SQLException | NullPointerException e) {
              e.printStackTrace();
            }
          }
        }
      }).build(new CacheLoader<Integer, Game>() {
        @Override
        public Game load(Integer key) {
          String gameFinder = "select board from in_progress where "
              + "player1 = ? or player2 = ?;";
          try (ResultSet rs = Db.query(gameFinder, key, key)) {
            if (!rs.next()) {
              System.out
                  .print("Could not load game from database for user " + key);
              return null;
            }
            System.out
                .println("Successfully retrieved game from db for user " + key);
            return Game.deserialize(rs.getString(1));
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
