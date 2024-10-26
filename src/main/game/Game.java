package main.game;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.ActionsInput;
import fileio.GameInput;
import fileio.Input;

public class Game {
	private final Player firstPlayer;
	private final Player secondPlayer;

	private int roundMana = 1;
	private int playerTurn;
	ObjectMapper objectMapper = new ObjectMapper();
	public Game(Input input, int gameNo) {
		GameInput gameInput = input.getGames().get(gameNo);

		firstPlayer = new Player(gameInput.getStartGame()
										  .getPlayerOneHero(),
								 input.getPlayerOneDecks()
										 .getDecks()
										 .get(gameInput.getStartGame().getPlayerOneDeckIdx()),
								 gameInput.getStartGame().getShuffleSeed());
		secondPlayer = new Player(gameInput.getStartGame().getPlayerTwoHero(),
								 input.getPlayerTwoDecks()
										 .getDecks()
										 .get(gameInput.getStartGame().getPlayerTwoDeckIdx()),
								 gameInput.getStartGame().getShuffleSeed());
		playerTurn = gameInput.getStartGame().getStartingPlayer();
	}

	public ObjectNode handleAction(ActionsInput actionsInput) {
		ObjectNode objectNode = objectMapper.createObjectNode();

		objectNode.put("command", actionsInput.getCommand());

		switch (actionsInput.getCommand()) {
			case "getPlayerDeck":
				objectNode.put("playerIdx", actionsInput.getPlayerIdx());
				Player playerDeck = getPlayer(actionsInput.getPlayerIdx());

				if (playerDeck != null) {
					objectNode.put("output", playerDeck.getDeck().toArrayNode());
					return objectNode;
				}
				break;
			case "getPlayerTurn":
				objectNode.put("output", playerTurn);
				return objectNode;
			case "getPlayerHero":
				objectNode.put("playerIdx", actionsInput.getPlayerIdx());
				Player playerHero = getPlayer(actionsInput.getPlayerIdx());

				if (playerHero != null) {
					objectNode.put("output", playerHero.getHero().toObjectNode(true));
					return objectNode;
				}
				break;

		}

		return null;
	}


	private void incrementMana() {
		if (roundMana == 10)
			return;

		roundMana++;
	}

	public Player getPlayer(int idx) {
		if (idx == 1)
			return firstPlayer;
		if (idx == 2)
			return secondPlayer;

		return null;
	}

	@Override
	public String toString() {
		return "Game{" +
				"firstPlayer=" + firstPlayer +
				", secondPlayer=" + secondPlayer +
				'}';
	}

	public Player getFirstPlayer() {
		return firstPlayer;
	}

	public Player getSecondPlayer() {
		return secondPlayer;
	}

}
