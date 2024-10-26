package main.game.action;

import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.ActionsInput;
import main.game.Game;

public class GetPlayerWins extends Action {
	public GetPlayerWins(ActionsInput actionsInput, Game game) {
		super(actionsInput, game);
	}

	@Override
	public ObjectNode execute() {
		ObjectNode objectNode = objectMapper.createObjectNode();

		objectNode.put("command", actionsInput.getCommand());
		int playerOneWins = Game.getPlayerOneWins();
		int playerTwoWins = Game.getPlayerTwoWins();
		int totalGames = Game.getTotalGames();

		if (actionsInput.getCommand().equals("getPlayerOneWins")) {
			objectNode.put("output", playerOneWins);
		}

		if (actionsInput.getCommand().equals("getPlayerTwoWins")) {
			objectNode.put("output", playerTwoWins);
		}

		if (actionsInput.getCommand().equals("getTotalGamesPlayed")) {
			objectNode.put("output", totalGames);
		}

		return objectNode;
	}
}
