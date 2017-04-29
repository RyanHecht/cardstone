package logins;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.google.gson.JsonObject;

public class ClassByJosh {

	public static int createNewGame(int user1, int user2){
		int gId = -1;
		ResultSet rs = null;
		try {
			Db.update("insert into inProgress values (null, ?,?)", user1, user2);
			rs = Db.query("select id from inProgress where user = ? order by id desc limit 1", user1);
			while (rs.next()) {
				gId = rs.getInt(1);
			}
		} catch (NullPointerException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally{
			try {
				rs.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return gId;
	}
	
	public static void insertBoardStateIntoDb(JsonObject boardState,int gameId){
		try {
			putBoardStateInGame(boardState,gameId);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static void putBoardStateInGame(JsonObject boardState, int gameId) throws SQLException {
		int eventNum = -1;
		ResultSet rs = null;
		try {
			rs = Db.query("select event from game_event where game = ? order by event desc limit 1", gameId);
			while(rs.next()){
				eventNum = rs.getInt(1);
			}
			Db.update("insert into game_event values(?,?,?)", gameId,eventNum + 1,boardState.toString());
		} catch (NullPointerException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally{
			rs.close();
		}
		
		
		
	}

	public static void endGame(Game game, int winnerNumber) {
		// TODO calc game metadata, transfer in progress to done
		
	}
	
}
