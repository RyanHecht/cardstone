package edu.brown.cs.jpattiz.boggle;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.regex.Pattern;
import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Sets;

import cardgamelibrary.Board;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import joptsimple.OptionSpec;

import freemarker.template.Configuration;
import spark.ExceptionHandler;
import spark.Spark;
import spark.ModelAndView;
import spark.QueryParamsMap;
import spark.Request;
import spark.Response;
import spark.TemplateViewRoute;
import spark.template.freemarker.FreeMarkerEngine;

public class Main {
	
	Board board;
	
  public static void main(String[] args) {
    OptionParser parser = new OptionParser();
    
    Main main = new Main();
    main.run();
      
  }

  
  
  private void run() {
	  board = new Board();
	  runSparkServer();
}



private void runSparkServer() {
    // We need to serve some simple static files containing CSS and JavaScript.
    // This tells Spark where to look for urls of the form "/static/*".
    Spark.externalStaticFileLocation("src/main/resources/static");

    // Development is easier if show exceptions in the browser.
    Spark.exception(Exception.class, new ExceptionPrinter());

    // We're render our responses with the FreeMaker template system.
    FreeMarkerEngine freeMarker = createEngine();

    Spark.get("/play", new PlayHandler(), freeMarker);
  }

  private class PlayHandler implements TemplateViewRoute {
    @Override
    public ModelAndView handle(Request req, Response res) {
      
      Map<String, Object> variables =
        ImmutableMap.of("title", "Boggle: Play a board",
                        "board", board);
      return new ModelAndView(variables, "play.ftl");
    }
  }
  
  private static FreeMarkerEngine createEngine() {
    Configuration config = new Configuration();
    File templates = new File("src/main/resources/spark/template/freemarker");
    try {
      config.setDirectoryForTemplateLoading(templates);
    } catch (IOException ioe) {
      System.out.printf("ERROR: Unable use %s for template loading.\n",
                        templates);
      System.exit(1);
    }
    return new FreeMarkerEngine(config);
  }

  private static final int INTERNAL_SERVER_ERROR = 500;
  private static class ExceptionPrinter implements ExceptionHandler {
    @Override
    public void handle(Exception e, Request req, Response res) {
      res.status(INTERNAL_SERVER_ERROR);
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
