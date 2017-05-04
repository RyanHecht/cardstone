package main;

import freemarker.template.Configuration;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import lobby.LobbyHandlers;
import server.CommsWebSocket;
import server.LobbyWebSocket;
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

    // this was throwing errors, there is no constructor Game()
    // Game g1 = new Game();
    // g1.setBoard(b1);
    // System.out.println(g1.jsonifySelf());
    FreeMarkerEngine f = createEngine();
    runSparkServer(80, f);

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
    Spark.webSocket("/lobbySocket", LobbyWebSocket.class);
    Spark.get("/listLobbies", new LobbyHandlers.ListLobbies());
    Spark.post("/joinLobby", new LobbyHandlers.JoinLobby());
    Spark.post("/spectateJoin", new LobbyHandlers.SpectateJoinLobby());
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
