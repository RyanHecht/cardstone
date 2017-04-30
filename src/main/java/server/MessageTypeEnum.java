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
  ANIMATION, // type: string id1: number id2: number

  // Explicitly defines animation to play (Server -> Client)
  // 3
  EXPLICIT_ANIMATION, // \\

  // A user targeted a card with another card (Client -> Server)
  // 4
  TARGETED_CARD, // iid1 iid2

  // A user targeted a player with a card (Client -> Server)
  // 5
  TARGETED_PLAYER, // word "self" or word "opponent"

  // A user attempted to play a card (Client -> Server)
  // 6
  ATTEMPTED_TO_PLAY, // instance id of card they're playing

  // the server asks a player to choose from a list of cards (Server -> Client)
  // 7
  CHOOSE_REQUEST, // list of card objects

  // client responds to choose request (Client -> Server)
  // 8
  CHOOSE_RESPONSE, // iid of chosen card

  // the server asks a player to target something (Server -> Client)
  // 9
  TARGET_REQUEST, // \\

  // client responds to target request (Client -> Server)
  // 10
  TARGET_RESPONSE, // \\

  // A client's action was okay (Server -> Client)
  // 11
  ACTION_OK,

  // A client's action was not okay (Server -> Client)
  // 12
  ACTION_BAD, // message: string

  // Response containing the user's Id (Client -> Server)
  // 13
  ID_RESPONSE,

  // Player has ended their turn (Client -> Server)
  // 14
  TURN_END,

  // Send a textual message to the client (Server -> Client)
  // 15
  TEXT_MESSAGE,

  // Player sends an in-game chat message (Client -> Server)
  // 16
  PLAYER_SEND_CHAT,

  // Server alerts player of new chat message (Server -> Client)
  // 17
  RECEIVE_CHAT,

  // Alert user of turn start (Server -> Client)
  // 18
  TURN_START,

  // Become a spectator (Server -> Client)
  // 19
  IS_SPECTATOR

}
