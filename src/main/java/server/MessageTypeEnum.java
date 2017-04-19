package server;

public enum MessageTypeEnum {

  // Updates of board state (Server -> Client)
  // 0
  BOARD_STATE,

  // whether the client understood the sent board state (Client -> Server)
  // 1
  UNDERSTOOD_BOARD_STATE,

  // predefined animation to play (Server -> Client)
  // 2
  ANIMATION,

  // Explicitly defines animation to play (Server -> Client)
  // 3
  EXPLICIT_ANIMATION,

  // A user targeted a card with another card (Client -> Server)
  // 4
  TARGETED_CARD,

  // A user targeted a player with a card (Client -> Server)
  // 5
  TARGETED_PLAYER,

  // A user attempted to play a card (Client -> Server)
  // 6
  ATTEMPTED_TO_PLAY,

  // the server asks a player to choose from a list of cards (Server -> Client)
  // 7
  CHOOSE_REQUEST,

  // client responds to choose request (Client -> Server)
  // 8
  CHOOSE_RESPONSE,

  // the server asks a player to target something (Server -> Client)
  // 9
  TARGET_REQUEST,

  // client responds to target request (Client -> Server)
  // 10
  TARGET_RESPONSE,

  // A client's action was okay (Server -> Client)
  // 11
  ACTION_OK,

  // A client's action was not okay (Server -> Client)
  // 12
  ACTION_BAD,

  // Response containing the user's Id (Client -> Server)
  // 13
  ID_RESPONSE

}
