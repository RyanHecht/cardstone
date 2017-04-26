package main;

import cardgamelibrary.Board;
import cardgamelibrary.OrderedCardCollection;
import cardgamelibrary.Zone;
import cards.SkyWhaleCreature;
import cards.StubCreature;
import freemarker.template.Configuration;
import game.Player;
import game.PlayerType;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import lobby.LobbyHandlers;
import logins.Gui;
import server.CommsWebSocket;
import spark.ExceptionHandler;
import spark.Request;
import spark.Response;
import spark.Spark;
import spark.template.freemarker.FreeMarkerEngine;

public class Main {
  private String[] args;
  private Gui gui;

  private Main(String[] args) {
    this.args = args;
  }

  public static void main(String[] args) {
    new Main(args).run();
  }

  private void run() {
    Player pOne = new Player(30, PlayerType.PLAYER_ONE, 0);
    Player pTwo = new Player(30, PlayerType.PLAYER_TWO, 1);
    OrderedCardCollection deckOne = new OrderedCardCollection(Zone.DECK, pOne);
    OrderedCardCollection deckTwo = new OrderedCardCollection(Zone.DECK, pTwo);
    Board b1 = new Board(deckOne, deckTwo);
    StubCreature c1 = new StubCreature(pOne);
    StubCreature c2 = new StubCreature(pTwo);
    SkyWhaleCreature c3 = new SkyWhaleCreature(pOne);
    OrderedCardCollection playerOneCreatures = new OrderedCardCollection(
        Zone.CREATURE_BOARD, pOne);
    OrderedCardCollection playerTwoCreatures = new OrderedCardCollection(
        Zone.CREATURE_BOARD, pTwo);

    playerOneCreatures.add(c1);
    playerOneCreatures.add(c3);
    playerTwoCreatures.add(c2);

    b1.setOcc(playerOneCreatures);
    b1.setOcc(playerTwoCreatures);

    SkyWhaleCreature c4 = new SkyWhaleCreature(pTwo);
    OrderedCardCollection playerOneCreatures2 = new OrderedCardCollection(
        Zone.CREATURE_BOARD, pOne);
    OrderedCardCollection playerTwoCreatures2 = new OrderedCardCollection(
        Zone.CREATURE_BOARD, pTwo);

    playerOneCreatures2.add(c1);
    playerOneCreatures2.add(c3);
    playerTwoCreatures2.add(c2);
    playerTwoCreatures2.add(c4);
    playerOneCreatures2.add(c3);
    playerOneCreatures2.add(c3);
    b1.setOcc(playerOneCreatures2);
    b1.setOcc(playerTwoCreatures2);
    // this was throwing errors, there is no constructor Game()
    // Game g1 = new Game();
    // g1.setBoard(b1);
    // System.out.println(g1.jsonifySelf());
    FreeMarkerEngine f = createEngine();
    runSparkServer(4567, f);

    gui = new Gui(f);
    gui.init();
  }

  private static FreeMarkerEngine createEngine() {
    Configuration config = new Configuration();
    File templates = new File("src/main/resources/spark/template/freemarker");
    try {
      config.setDirectoryForTemplateLoading(templates);
    } catch (IOException ioe) {
      System.out.printf("ERROR: Unable use %s for template loading.%n",
          templates);
      System.exit(1);
    }
    return new FreeMarkerEngine(config);
  }

  private void runSparkServer(int port, FreeMarkerEngine freeMarker) {
    Spark.port(port);
    Spark.externalStaticFileLocation("src/main/resources/static");
    Spark.exception(Exception.class, new ExceptionPrinter());
    Spark.webSocket("/socket", CommsWebSocket.class);
    Spark.get("/listLobbies", new LobbyHandlers.ListLobbies());
    Spark.post("/joinLobby", new LobbyHandlers.JoinLobby());
    Spark.post("/makeLobby", new LobbyHandlers.MakeLobby());
    Spark.get("/makeLobbyTest", new LobbyHandlers.LobbyTesting());
  }

  private static class ExceptionPrinter implements ExceptionHandler {
    @Override
    public void handle(Exception e, Request req, Response res) {
      res.status(500);
      StringWriter stacktrace = new StringWriter();
      try (PrintWriter pw = new PrintWriter(stacktrace)) {
        pw.println("<pre>");
        e.printStackTrace(pw);
        pw.println("</pre>");
      }
      res.body(stacktrace.toString());
    }
  }
}
