package main.game.action;

import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.ActionsInput;
import main.game.Game;
import main.game.character.GameCharacter;

public class GetCardAtPosition extends Action {
	public GetCardAtPosition(ActionsInput actionsInput, Game game) {
		super(actionsInput, game);
	}

	@Override
	public ObjectNode execute() {
		ObjectNode objectNode = objectMapper.createObjectNode();

		objectNode.put("command", actionsInput.getCommand());
		GameCharacter cardAtPosition = game.getCard(actionsInput.getX(), actionsInput.getY());

		objectNode.put("x", actionsInput.getX());
		objectNode.put("y", actionsInput.getY());
		if (cardAtPosition == null) {
			objectNode.put("output", "No card available at that position.");
		} else {
			objectNode.put("output", cardAtPosition.getCard().toObjectNode(false));
		}

		return objectNode;
	}
}
