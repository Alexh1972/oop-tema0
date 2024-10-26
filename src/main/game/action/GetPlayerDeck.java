package main.game.action;

import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.ActionsInput;
import main.game.Game;
import main.game.Player;

public class GetPlayerDeck extends Action {
	public GetPlayerDeck(ActionsInput actionsInput, Game game) {
		super(actionsInput, game);
	}
	@Override
	public ObjectNode execute() {
		ObjectNode objectNode = objectMapper.createObjectNode();

		objectNode.put("command", actionsInput.getCommand());

		objectNode.put("playerIdx", actionsInput.getPlayerIdx());
		Player playerDeck = game.getPlayer(actionsInput.getPlayerIdx());

		if (playerDeck != null) {
			objectNode.put("output", playerDeck.getDeck().toArrayNode());
			return objectNode;
		}

		return null;
	}
}
