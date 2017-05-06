package game;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import com.google.gson.JsonObject;

import cardgamelibrary.Card;
import cardgamelibrary.Zone;
import events.CardPlayedEvent;
import events.TurnEndEvent;
import server.CommsWebSocket;

public class DemoGame extends Game {

	private static final int AI_ID = -10;

	private int actionId = 0;

	private final String[] messages = new String[11];

	private static TurnEndEvent turnEnd;

	private static CardPlayedEvent aiPlayedElement;

	private static CardPlayedEvent aiPlayedWaterSpirit;

	private static JsonObject aiTurnEnd = new JsonObject();

	private static JsonObject aiWaterPlayed = new JsonObject();;

	private static JsonObject aiWaterSpiritPlayed = new JsonObject();;

	public DemoGame(int playerOneId) {
		// superclass constructor with "true" flag passed to indicate that it is a
		// tutorial.
		super(getFirstPlayerDeck(), getSecondPlayerDeck(), playerOneId, AI_ID, true);
		messages[0] = "Play a Water Element from your hand by dragging it anywhere on your side of the board. You will see your elemental "
				+ "resource pool update in the bottom left corner of your screen.";
		messages[1] = "Play the Water Spirit from your hand by dragging it anywhere on your side of the board. Unfortunately, creatures "
				+ "cannot attack on the turn they are played.";
		messages[2] = "End your turn by clicking the 'End Turn' button in the middle of your screen";
		messages[3] = "Attack the enemy Water Spirit by clicking on your Water Spirit and dragging to the opponent's Water Spirit";
		messages[4] = "Play another Water Element from your hand by dragging it anywhere on your side of the board.";
		messages[5] = "Play the Delve The Depths from your hand by dragging it anywhere on your side of the board. "
				+ " You will have to select a card from the popup by clicking on it.";
		messages[6] = "Play another Water Element from your hand.";
		messages[7] = "Play the Riptide you added to your hand targeting the opponent's Water Spirit by dragging Riptide "
				+ "onto the Water Spirit.";
		messages[8] = "End your turn.";
		messages[9] = "Attack your opponent with your Water Spirit by clicking on it and dragging it to the heart representing your "
				+ "opponent's health in the top right corner.";
		messages[10] = "Congrats! You have finished the tutorial.";

		// create the AI events.
		Player ai = getBoard().getInactivePlayer();
		turnEnd = new TurnEndEvent(ai);
		// these are the two cards the ai plays.
		Card aiWaterSpirit = getBoard().getOcc(ai, Zone.HAND).getCards().get(0);
		Card aiWaterElement = getBoard().getOcc(ai, Zone.HAND).getCards().get(1);

		System.out.println("The water spirit card for the AI has this name: " + aiWaterSpirit.getName());
		System.out.println("The water element card for the AI has this name: " + aiWaterElement.getName());

		// events that reflect the cards played are constructed.
		aiPlayedElement = new CardPlayedEvent(aiWaterElement, getBoard().getOcc(ai, Zone.HAND),
				getBoard().getOcc(ai, Zone.GRAVE));
		aiPlayedWaterSpirit = new CardPlayedEvent(aiWaterSpirit, getBoard().getOcc(ai, Zone.HAND),
				getBoard().getOcc(ai, Zone.CREATURE_BOARD));

		// build ai JsonObjects.
		// don't actually have to do anything with the turn end object, it can
		// remain empty.
		aiWaterPlayed = null;

		aiWaterSpiritPlayed = null;
	}

	@Override
	public void handleTurnend(JsonObject userInput, int playerId) {
		System.out.println("CURRENT ACTION ID: " + actionId);
		if (playerId == AI_ID) {
			super.handleTurnend(userInput, playerId);
		} else {
			if (actionId != 2 && actionId != 8) {
				sendPlayerActionBad(playerId, messages[actionId]);
				return;
			}
			// in this case the turn end was correct so we should execute it.
			super.handleTurnend(userInput, playerId);

			// now we must decide what the ai needs to dp.
			if (actionId == 2) {
				// ai plays element, then spirit, then ends turn.
				act(aiPlayedElement);
				act(aiPlayedWaterSpirit);
				act(turnEnd);

				actionId++;
				try {
					CommsWebSocket.sendTurnStart(playerId, true);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				// have to manually send board.
				sendWholeBoardToAllAndDb();
			} else if (actionId == 8) {
				// in this case the AI just ends their turn.
				act(turnEnd);

				actionId++;

				// manually send board.
				sendWholeBoardToAllAndDb();
			}

			actionId++;
			// tell player what to do next.
			sendPlayerTextMessage(playerId, messages[actionId]);
		}
	}

	@Override
	public void handleCardTargeted(JsonObject userInput, int playerId) {
		System.out.println("CURRENT ACTION ID: " + actionId);
		if (playerId == AI_ID) {
			super.handleCardTargeted(userInput, playerId);
		} else {
			if (actionId != 3 && actionId != 7) {
				sendPlayerActionBad(playerId, messages[actionId]);
				return;
			}
			Card targeter = getBoard().getCardById(userInput.get("IID1").getAsInt());
			Card targeted = getBoard().getCardById(userInput.get("IID2").getAsInt());
			switch (actionId) {
			case 3:
				if (targeter.getName().equals("Water Spirit") && targeted.getName().equals("Water Spirit")) {
					System.out.println("SENDING THE WATER SPIRIT COMBAT EVENT");
					// increment actionId
					actionId++;
					super.handleCardTargeted(userInput, playerId);
				} else {
					sendPlayerActionBad(playerId, messages[actionId]);
				}
				break;
			case 7:
				if (targeter.getName().equals("Riptide") && targeted.getName().equals("Water Spirit")) {
					// increment actionId
					actionId++;
					super.handleCardTargeted(userInput, playerId);
				} else {
					sendPlayerActionBad(playerId, messages[actionId]);
				}
				break;
			}
			// tell player what to do next.
			sendPlayerTextMessage(playerId, messages[actionId]);
		}
	}

	@Override
	public void handlePlayerTargeted(JsonObject userInput, int playerId) {
		System.out.println("CURRENT ACTION ID: " + actionId);
		if (playerId == AI_ID) {
			super.handlePlayerTargeted(userInput, playerId);
		} else {
			if (actionId != 9) {
				// tell the player that their action wasn't good and send the
				// appropriate message string.
				sendPlayerActionBad(playerId, messages[actionId]);
				return;
			}
			// increment actionId
			actionId++;
			super.handlePlayerTargeted(userInput, playerId);
			// tell player what to do next.
			sendPlayerTextMessage(playerId, messages[actionId]);
		}
	}

	@Override
	public void handleChosen(JsonObject userInput, int playerId) {
		System.out.println("CURRENT ACTION ID: " + actionId);
		if (playerId == AI_ID) {
			super.handleChosen(userInput, playerId);
		} else {
			super.handleChosen(userInput, playerId);
		}
	}

	@Override
	public void handleCardPlayed(JsonObject userInput, int playerId) {
		System.out.println("CURRENT ACTION ID: " + actionId);
		if (playerId == AI_ID) {
			super.handleCardPlayed(userInput, playerId);
		} else {
			if (actionId != 0 && actionId != 1 && actionId != 4 && actionId != 5 && actionId != 6) {
				// tell the player that their action wasn't good and send the
				// appropriate message string.
				sendPlayerActionBad(playerId, messages[actionId]);
				return;
			}
			Card c = getBoard().getCardById(userInput.get("IID1").getAsInt());
			switch (actionId) {
			case 0:
				if (c.getName().equals("water")) {
					// increment actionId
					actionId++;
					// they have done the correct thing so we should execute it.
					super.handleCardPlayed(userInput, playerId);
				} else {
					sendPlayerActionBad(playerId, messages[actionId]);
				}
				break;
			case 1:
				if (c.getName().equals("Water Spirit")) {
					// increment actionId
					actionId++;
					// they have performed the correct action!
					super.handleCardPlayed(userInput, playerId);
				} else {
					sendPlayerActionBad(playerId, messages[actionId]);
				}
				break;
			case 4:
				if (c.getName().equals("water")) {
					// increment actionId
					actionId++;
					// they have done the correct thing so we should execute it.
					super.handleCardPlayed(userInput, playerId);
				} else {
					sendPlayerActionBad(playerId, messages[actionId]);
				}
				break;
			case 5:
				if (c.getName().equals("Delve The Depths")) {
					// increment actionId
					actionId++;
					// they have done the correct thing so we should execute it.
					super.handleCardPlayed(userInput, playerId);
				} else {
					sendPlayerActionBad(playerId, messages[actionId]);
				}
				break;
			case 6:
				if (c.getName().equals("water")) {
					// increment actionId
					actionId++;
					// they have done the correct thing so we should execute it.
					super.handleCardPlayed(userInput, playerId);
				} else {
					sendPlayerActionBad(playerId, messages[actionId]);
				}
				break;
			}
			// tell player what to do next.
			sendPlayerTextMessage(playerId, messages[actionId]);
		}
	}

	/**
	 * Gets the first player's deck as a list of strings.
	 * 
	 * @return a list of strings representing the deck.
	 */
	private static List<String> getFirstPlayerDeck() {
		LinkedList<String> firstPlayerCards = new LinkedList<String>();
		// starting hand will be 6 water elements, first card they draw will be
		// water spirit.
		firstPlayerCards.addLast("Water Spirit");
		firstPlayerCards.addLast("Water Element");
		firstPlayerCards.addLast("Water Element");
		firstPlayerCards.addLast("Water Element");
		firstPlayerCards.addLast("Water Element");
		firstPlayerCards.addLast("Water Element");
		firstPlayerCards.addLast("Water Element");

		// the next card the user will draw (start of 2nd turn) will be Delve The
		// Depths.
		firstPlayerCards.addLast("Delve The Depths");

		// now just give them a bunch of riptide cards.
		firstPlayerCards.addLast("Riptide");
		firstPlayerCards.addLast("Riptide");
		firstPlayerCards.addLast("Riptide");
		firstPlayerCards.addLast("Riptide");
		firstPlayerCards.addLast("Riptide");
		firstPlayerCards.addLast("Riptide");
		firstPlayerCards.addLast("Riptide");
		firstPlayerCards.addLast("Riptide");
		firstPlayerCards.addLast("Riptide");
		firstPlayerCards.addLast("Riptide");
		firstPlayerCards.addLast("Riptide");

		return firstPlayerCards;
	}

	/**
	 * Gets the second player's (not human) deck as a list of strings.
	 * 
	 * @return a list of strings representing the deck.
	 */
	private static List<String> getSecondPlayerDeck() {
		// our current implementation of the demo game allows us to give both
		// players the same decks.
		// this can obviously be swapped out as improvements to the tutorial system
		// are made!
		return getFirstPlayerDeck();
	}

	/**
	 * Used to get the current action needed for the user.
	 * 
	 * @return the message the user should see.
	 */
	public String getMessage() {
		return messages[actionId];
	}

}
