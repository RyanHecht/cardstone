package server;

public enum MessageTypeEnum {

  // Updates of board state (Server -> Client)
  BOARD_STATE,
  // Animations to play (Server -> Client)
  ANIMATION,
  // A user's move (Client -> Server)
  USER_INPUT,
  // Request for the user's Id (Server -> Client)
  ID_REQUEST,
  // Response containing the user's Id (Client -> Server)
  ID_RESPONSE

}
