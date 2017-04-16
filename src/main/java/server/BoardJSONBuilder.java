package server;

import com.google.gson.JsonObject;

import cardgamelibrary.Board;

public class BoardJSONBuilder {

	public static JsonObject jsonifyBoard(Board b){
		JsonObject result = new JsonObject();
		result.addProperty("type", String.valueOf(MessageTypeEnum.BOARD_STATE));
		JsonObject payload = new JsonObject();
		payload.addProperty("p1Health", );
		
		return result;
	}
	
}
