package server;

public enum MessageTypeEnum {

  // Updates of board state (Server -> Client)
  BOARD_STATE,

  // whether the client understood the sent board state (Client -> Server)
  UNDERSTOOD_BOARD_STATE,

  // predefined animation to play (Server -> Client)
  ANIMATION,

  // Explicitly defines animation to play (Server -> Client)
  EXPLICIT_ANIMATION,

  // A user targeted a card with another card (Client -> Server)
  TARGETED_CARD,

  // A user targeted a player with a card (Client -> Server)
  TARGETED_PLAYER,

  // A user attempted to play a card (Client -> Server)
  ATTEMPTED_TO_PLAY,

  // the server asks a player to choose from a list of cards (Server -> Client)
  CHOOSE_REQUEST,

  // client responds to choose request (Client -> Server)
  CHOOSE_RESPONSE,

  // the server asks a player to target something (Server -> Client)
  TARGET_REQUEST,

  // client responds to target request (Client -> Server)
  TARGET_RESPONSE,

  // whether a client's action was okay or not (Server -> Client)
  ACTION_OK,

  // Request for the user's Id (Server -> Client)
  ID_REQUEST,

  // Response containing the user's Id (Client -> Server)
  ID_RESPONSE

}
