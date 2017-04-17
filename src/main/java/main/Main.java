package main;

import cardgamelibrary.Board;
import cardgamelibrary.OrderedCardCollection;
import cardgamelibrary.Zone;
import cards.SkyWhaleCreature;
import cards.StubCreature;
import game.Player;
import game.PlayerType;

public class Main {
	private String[] args;

	private Main(String[] args) {
		this.args = args;
	}

	public static void main(String[] args) {
		new Main(args).run();
	}

	private void run() {
		Player pOne = new Player(30, PlayerType.PLAYER_ONE);
		Player pTwo = new Player(30, PlayerType.PLAYER_TWO);
		OrderedCardCollection deckOne = new OrderedCardCollection(Zone.DECK, pOne);
		OrderedCardCollection deckTwo = new OrderedCardCollection(Zone.DECK, pTwo);
		Board b1 = new Board(deckOne, deckTwo);
		StubCreature c1 = new StubCreature(pOne);
		StubCreature c2 = new StubCreature(pTwo);
		SkyWhaleCreature c3 = new SkyWhaleCreature(pOne);
		OrderedCardCollection playerOneCreatures = new OrderedCardCollection(Zone.CREATURE_BOARD, pOne);
		OrderedCardCollection playerTwoCreatures = new OrderedCardCollection(Zone.CREATURE_BOARD, pTwo);

		playerOneCreatures.add(c1);
		playerOneCreatures.add(c3);
		playerTwoCreatures.add(c2);

		b1.setOcc(playerOneCreatures);
		b1.setOcc(playerTwoCreatures);
	}
}
