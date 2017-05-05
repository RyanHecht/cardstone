package game;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class ReplayEvent {
  private final JsonObject board;
  private final JsonArray animations;

  public ReplayEvent(JsonObject board, JsonArray animations) {
    this.board = board;
    this.animations = animations;
  }

  public JsonObject getBoard() {
    return board;
  }

  public JsonArray getAnimations() {
    return animations;
  }
}
