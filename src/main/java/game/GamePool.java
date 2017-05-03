package game;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Base64;
import java.util.concurrent.ExecutionException;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.cache.RemovalListener;
import com.google.common.cache.RemovalNotification;

import logins.Db;

/**
 * Represents pool of all games that are going on.
 * @author ryan an willayyy
 */
public class GamePool {
  private static final int POOL_SIZE = 100;
  private static LoadingCache<Integer, Game> playersToGames = CacheBuilder
      .newBuilder()
      .maximumSize(POOL_SIZE)
      .removalListener(new RemovalListener<Integer, Game>() {
        public void onRemoval(RemovalNotification<Integer, Game> removal) {
          if (removal.wasEvicted()) {
            int id = removal.getKey();
            Game g = removal.getValue();

            int otherId = g.getOpposingPlayerId(id);
            if (id > otherId) {
              id = id ^ otherId ^ (otherId = id); // swaps values
            }

            String dbCheck = "select id from in_progress where player1 = ?;";
            try (ResultSet rs = Db.query(dbCheck, id)) {
              String serialG = serialize(g);
              // if this game isn't in db, insert it; otherwise, update it
              if (!rs.next()) {
                Db.update("insert into in_progress values(null, ?, ?, ?);", id,
                    otherId, serialG);
              } else {
                Db.update("update in_progress set board = ? where player1 = ?;",
                    serialG, id);
              }
            } catch (IOException | SQLException | NullPointerException e) {
              e.printStackTrace();
            }
          }
        }
      }).build(new CacheLoader<Integer, Game>() {
        public Game load(Integer key) {
          String gameFinder = "select board from in_progress where "
              + "player1 = ? or player2 = ?;";
          try (ResultSet rs = Db.query(gameFinder, key)) {
            rs.next();
            Game found = deserialize(rs.getString(1));
            assert !rs.next();
            return found;
          } catch (SQLException | NullPointerException | ClassNotFoundException
              | IOException e) {
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
    try {
      g1 = playersToGames.get(u1) == null ? gId
          : playersToGames.get(u1).getId();
      g2 = playersToGames.get(u2) == null ? gId
          : playersToGames.get(u2).getId();
    } catch (ExecutionException e) {
      g1 = gId;
      g2 = g1;
    }

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

  public Game getGameByPlayerId(int id) {
    try {
      return playersToGames.get(id);
    } catch (ExecutionException e) {
      return null;
    }
  }

  private static String serialize(Game g) throws IOException {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    ObjectOutputStream oos = new ObjectOutputStream(baos);
    oos.writeObject(g);
    oos.close();
    return Base64.getEncoder().encodeToString(baos.toByteArray());
  }

  private static Game deserialize(String s)
      throws IOException, ClassNotFoundException {
    byte[] data = Base64.getDecoder().decode(s);
    ObjectInputStream ois = new ObjectInputStream(
        new ByteArrayInputStream(data));
    Object o = ois.readObject();
    ois.close();
    return (Game) o;
  }
}
