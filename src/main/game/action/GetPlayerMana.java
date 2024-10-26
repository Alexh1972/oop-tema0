package main.game.action;

import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.ActionsInput;
import main.game.Game;
import main.game.Player;

public class GetPlayerMana extends Action {
	public GetPlayerMana(ActionsInput actionsInput, Game game) {
		super(actionsInput, game);
	}
	@Override
	public ObjectNode execute() {
		ObjectNode objectNode = objectMapper.createObjectNode();

		objectNode.put("command", actionsInput.getCommand());
		objectNode.put("playerIdx", actionsInput.getPlayerIdx());

		Player playerMana = game.getPlayer(actionsInput.getPlayerIdx());
		if (playerMana != null) {
			objectNode.put("output", playerMana.getMana());
			return objectNode;
		}

		return null;
	}
}
