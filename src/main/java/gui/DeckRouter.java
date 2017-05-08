package gui;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.common.collect.ImmutableMap;
import com.google.gson.JsonObject;

import spark.ModelAndView;
import spark.QueryParamsMap;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.Spark;
import spark.template.freemarker.FreeMarkerEngine;

public class DeckRouter implements RouteGroup {
  private final FreeMarkerEngine fm;

  public DeckRouter(FreeMarkerEngine fm) {
    this.fm = fm;
  }

  @Override
  public void registerRoutes() {
    Spark.post("/deck_upload", new UploadHandler());
    Spark.get("/decks", new DecksHandler(), fm); // displaying all decks
    Spark.get("/deck", new DeckHandler(), fm); // displaying one deck
    Spark.post("/deck_from", new DeckFinder());
  }

  private class UploadHandler implements Route {
    @Override
    public String handle(Request req, Response res) {
      QueryParamsMap qm = req.queryMap();

      String deck = qm.value("deck");
      String deckName = qm.value("name");
      String uid = req.cookie("id");

      String deckSearch = "select id from deck where name = ? and user = ?;";
      try (ResultSet rs = Db.query(deckSearch, deckName, uid)) {
        if (rs.next()) {
          Db.update("update deck set cards = ? where id = ?;", deck,
              rs.getInt(1));
          System.out.println("Overrode previous deck " + deckName);
        } else {
          Db.update("insert into deck values(null, ?, ?, ?);", deckName, uid,
              deck);
          System.out.println("Inserted deck " + deckName);
        }
      } catch (NullPointerException | SQLException e) {
        e.printStackTrace();
      }

      // we don't do anything with callback
      return null;
    }
  }

  private static class DecksHandler implements AuthRoute {
    @Override
    public ModelAndView customHandle(Request req, Response res) {
      String username = req.cookie("username");
      List<String> decks = new ArrayList<>();
      try (ResultSet rs = Db.query("select name from deck where user=?;",
          req.cookie("id"))) {
        while (rs.next()) {
          decks.add(rs.getString(1));
        }
      } catch (NullPointerException | SQLException e) {
        e.printStackTrace();
      }

      Map<String, Object> vars = ImmutableMap.of("title",
          "Cardstone: The Shattering", "username", username, "decks", decks);
      return new ModelAndView(vars, "decks.ftl");
    }
  }

  private static class DeckHandler implements AuthRoute {
    @Override
    public ModelAndView customHandle(Request req, Response res) {
      String queryString = req.queryString();
      int deckIndex = queryString.lastIndexOf('=');
      if (deckIndex == -1) {
        return new ModelAndView(
            ImmutableMap.of("title", "Cardstone: The Shattering"), "deck.ftl");
      }

      String deckName = queryString.substring(deckIndex + 1).replace('_', ' ');
      String cards = "Deck " + deckName + " doesn't exist.";

      String deckQuery = "select cards from deck where user=? and name=?;";
      try (ResultSet rs = Db.query(deckQuery, req.cookie("id"), deckName)) {
        if (rs.next()) {
          cards = rs.getString(1);
        }

        assert !rs.next();
      } catch (NullPointerException | SQLException e) {
        e.printStackTrace();
      }

      Map<String, Object> vars = ImmutableMap.of("title",
          "Cardstone: The Shattering", "deckname", deckName, "deck", cards);
      return new ModelAndView(vars, "deck.ftl");
    }
  }

  // Finds deck given its name and user
  private static class DeckFinder implements Route {
    @Override
    public String handle(Request req, Response res) {
      JsonObject json = new JsonObject();
      QueryParamsMap qm = req.queryMap();

      String deckName = qm.value("deck");
      String uid = req.cookie("id");
      String deckQuery = "select cards from deck where name = ? and user = ?;";
      try (ResultSet rs = Db.query(deckQuery, deckName, uid)) {
        rs.next();
        json.addProperty("cards", rs.getString(1));
        assert !rs.next();
      } catch (NullPointerException | SQLException e) {
        e.printStackTrace();
      }

      return json.toString();
    }
  }
}
