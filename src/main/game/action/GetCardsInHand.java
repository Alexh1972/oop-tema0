package main.game.action;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.ActionsInput;
import main.game.Game;
import main.game.Player;

public class GetCardsInHand extends Action {
	public GetCardsInHand(ActionsInput actionsInput, Game game) {
		super(actionsInput, game);
	}
	@Override
	public ObjectNode execute() {
		ObjectNode objectNode = objectMapper.createObjectNode();

		objectNode.put("command", actionsInput.getCommand());
		objectNode.put("playerIdx", actionsInput.getPlayerIdx());
		Player playerHand = game.getPlayer(actionsInput.getPlayerIdx());

		if (playerHand != null) {
			objectNode.put("output", playerHand.getHand().toArrayNode());
			return objectNode;
		}

		return null;
	}
}
