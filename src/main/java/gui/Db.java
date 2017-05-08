package gui;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import game.Game;
import game.GameManager;

/**
 * A database class for shit.
 *
 * @author wriley1
 */
public final class Db {
  private static ThreadLocal<CachedConnection> instances = new ThreadLocal<>();
  private static final String PATH = "data/users/users.sqlite3";

  private Db() {
    // Don't instantiate
  }

  /**
   * Closes the current connection and the cache of prepared statements.
   */
  public static void close() {
    if (!(instances.get() == null)) {
      instances.get().close();
    }
  }

  /**
   * Closes the current connection and the cache of prepared statements.
   */
  public static void open() {
    if (instances.get() == null) {
      instances.set(new CachedConnection(PATH));
    }
  }

  /**
   * Returns a ResultSet from a query to the DbConnector's database.
   *
   * @param query the base query from which to make a PreparedStatement
   * @param options a String[] of options to feed into the query
   * @return the results from the query, as a ResultSet
   * @throws SQLException if there's a problem executing/processing the query
   * @throws NullPointerException if there's no db to query on
   */
  public static ResultSet query(String query, Object... options)
      throws SQLException, NullPointerException {
    if (instances.get() == null) {
      instances.set(new CachedConnection(PATH));
    }
    return instances.get().query(query, options);
  }

  /**
   * Executes a PreparedStatement from a base statement and bunch of options.
   * Meant for inserting things into the db, tweaking its settings – basically
   * anything except querying.
   *
   * @param update the base statement from which to make your update
   * @param options a String[] of options to feed into the statement
   * @throws SQLException if there's a problem executing/processing the update
   * @throws NullPointerException if there's no db to query on
   */
  public static void update(String update, Object... options)
      throws SQLException, NullPointerException {
    if (instances.get() == null) {
      instances.set(new CachedConnection(PATH));
    }
    instances.get().update(update, options);
  }

  public static void init() {
    try {
      Db.update("create table if not exists user("
          + "id integer primary key autoincrement, "
          + "username text unique not null, password text not null);");
      Db.update("create table if not exists deck("
          + "id integer primary key autoincrement, " + "name text not null, "
          + "user integer not null, " + "cards text not null, "
          + "UNIQUE(name, user), " + "FOREIGN KEY (user) REFERENCES user(id) "
          + "ON DELETE CASCADE ON UPDATE CASCADE);");
      Db.update("create table if not exists finished_game("
          + "id integer primary key, winner integer not null, "
          + "player1 integer not null, player2 integer not null, "
          + "moves integer, date text not null, "
          + "FOREIGN KEY (player1) REFERENCES user(id) "
          + "ON DELETE CASCADE ON UPDATE CASCADE, "
          + "FOREIGN KEY (player2) REFERENCES user(id) "
          + "ON DELETE CASCADE ON UPDATE CASCADE, UNIQUE(id, winner));");
      Db.update("create table if not exists in_progress("
          + "id integer primary key,"
          + "player1 integer not null, player2 integer not null, board blob, "
          + "FOREIGN KEY (player1) REFERENCES user(id) "
          + "ON DELETE CASCADE ON UPDATE CASCADE,"
          + "FOREIGN KEY (player2) REFERENCES user(id)"
          + "ON DELETE CASCADE ON UPDATE CASCADE, "
          + "UNIQUE(id, player1, player2));");
      Db.update("create table if not exists game_event("
          + "game integer not null, event integer not null,"
          + "board text not null, animations text not null, UNIQUE(game, event));");

      try (ResultSet rs = Db.query("select * from in_progress;")) {
        while (rs.next()) {
          int turns;
          String board = rs.getString(4);
          try {
            Game g = Game.deserialize(board);
            turns = g.getNumTurns();
            int p1 = g.getActivePlayerId();
            int p2 = g.getOpposingPlayerId(p1);
            System.out.println(
                String.format("Cleaning out game %d with players %d and %d",
                    g.getId(), p1, p2));
          } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
            turns = 0;
          }

          GameManager.registerFinishedGame(rs.getInt(1), rs.getInt(2),
              rs.getInt(3), 0, turns);
        }
      } catch (SQLException | NullPointerException e) {
        e.printStackTrace();
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  /**
   * The thing that ThreadLocal holds -- a connection as well as a cache of
   * prepared statements.
   * @author wriley1
   */
  private static final class CachedConnection {
    private static final String BASE_URL = "jdbc:sqlite:";

    private Connection conn;
    private final Map<String, PreparedStatement> queries;

    private CachedConnection(String path) {
      connect(path);
      queries = new HashMap<>();
    }

    private ResultSet query(String query, Object... options)
        throws SQLException {
      PreparedStatement base = prepFrom(query);
      int i = 1;
      for (Object o : options) {
        base.setObject(i, o);
        i++;
      }
      return base.executeQuery();
    }

    private void update(String update, Object... options) throws SQLException {
      PreparedStatement base = prepFrom(update);
      int i = 1;
      for (Object o : options) {
        base.setObject(i, o);
        System.out.println("Setting object: " + o);
        i++;
      }
      base.executeUpdate();
    }

    private PreparedStatement prepFrom(String query) throws SQLException {

      PreparedStatement base = queries.get(query);
      if (base == null) {
        base = conn.prepareStatement(query);

      }
      return base;
    }

    private void close() {
      try {
        for (PreparedStatement ps : queries.values()) {
          ps.close();
        }
        conn.close();
      } catch (SQLException e) {
        queries.clear();
        conn = null;
      }
    }

    private void connect(String path) {
      String url = BASE_URL + path;
      try {
        Class.forName("org.sqlite.JDBC");
        conn = DriverManager.getConnection(url);
        Statement stat = conn.createStatement();
        stat.executeUpdate("PRAGMA foreign_keys = ON;");
        stat.close();
      } catch (SQLException | ClassNotFoundException e) {
        e.printStackTrace();
        conn = null;
      }
    }
  }
}
