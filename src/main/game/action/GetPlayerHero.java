package main.game.action;

import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.ActionsInput;
import main.game.Game;
import main.game.Player;

public class GetPlayerHero extends Action {
	public GetPlayerHero(ActionsInput actionsInput, Game game) {
		super(actionsInput, game);
	}
	@Override
	public ObjectNode execute() {
		ObjectNode objectNode = objectMapper.createObjectNode();

		objectNode.put("command", actionsInput.getCommand());
		objectNode.put("playerIdx", actionsInput.getPlayerIdx());

		Player playerHero = game.getPlayer(actionsInput.getPlayerIdx());
		if (playerHero != null) {
			objectNode.put("output", playerHero.getHero().getCard().toObjectNode(true));
			return objectNode;
		}

		return null;
	}
}
