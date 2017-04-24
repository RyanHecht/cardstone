package logins;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

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
    System.out.println("Made it here");
    instances.get().update(update, options);
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
        System.out.println("I gotta connection");
        Statement stat = conn.createStatement();
        stat.executeUpdate("PRAGMA foreign_keys = ON;");
        stat.close();
        System.out.println("I successfully connected");
      } catch (SQLException | ClassNotFoundException e) {
        System.out.println("I couldn't connect");
        e.printStackTrace();
        conn = null;
      }
    }
  }
}
