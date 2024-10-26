package main.game.action;

import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.ActionsInput;
import main.game.Game;

public class GetCardsOnTable extends Action {
	public GetCardsOnTable(ActionsInput actionsInput, Game game) {
		super(actionsInput, game);
	}

	@Override
	public ObjectNode execute() {
		ObjectNode objectNode = objectMapper.createObjectNode();

		objectNode.put("command", actionsInput.getCommand());

		objectNode.put("output", game.boardToArrayNode());
		return objectNode;
	}
}
