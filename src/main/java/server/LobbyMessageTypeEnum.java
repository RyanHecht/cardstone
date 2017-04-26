package server;

public enum LobbyMessageTypeEnum {

  // On connection, joining (Client -> Server)
  // (not-host sends)
  // 0
  LOBBY_CONNECT,

  // On connection, creating (Client -> Server)
  // (host sends)
  // 1
  LOBBY_CREATE,

  // Alert the client that someone else has entered their lobby (Server ->
  // Client)
  // (not-host)
  // 2
  OPPONENT_ENTERED_LOBBY,

  // Opponent left lobby (Server -> Client).
  // 3
  // (not-host)
  OPPONENT_LEFT_LOBBY,

  // Self set deck (Client -> Server).
  // 4
  // (both)
  SELF_SET_DECK,

  // Opponent set deck (Server -> Client).
  // 5
  // (both)
  OPPONENT_SET_DECK,

  // Host starts the game (Client -> Server).
  // 6
  // (host)
  START_GAME_REQUEST,

  // Tell non-host the game is starting (Server -> Client).
  // 7
  GAME_IS_STARTING,

  // Lobby is cancelled (host left) (Server -> Client).
  // 8
  LOBBY_CANCELLED

}
