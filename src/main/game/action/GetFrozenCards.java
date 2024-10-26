package main.game.action;

import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.ActionsInput;
import main.game.Game;
import main.game.character.GameCharacter;

import java.util.ArrayList;

public class GetFrozenCards extends Action {

	public GetFrozenCards(ActionsInput actionsInput, Game game) {
		super(actionsInput, game);
	}

	@Override
	public ObjectNode execute() {
		ObjectNode objectNode = objectMapper.createObjectNode();

		objectNode.put("command", actionsInput.getCommand());
		objectNode.put("output", game.frozenCardsToArrayNode());

		return objectNode;
	}
}
