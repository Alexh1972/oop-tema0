package main.game.action;

import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.ActionsInput;
import main.game.Game;

public class CardAttack extends Action {
	public CardAttack(ActionsInput actionsInput, Game game) {
		super(actionsInput, game);
	}

	@Override
	public ObjectNode execute() {
		ObjectNode objectNode = objectMapper.createObjectNode();

		objectNode.put("command", actionsInput.getCommand());
		AttackingStatus attackingStatus = game.attackOnBoard(actionsInput.getCardAttacker(), actionsInput.getCardAttacked());
		if (attackingStatus == AttackingStatus.ATTACKING_STATUS_ALREADY_ATTACKED) {
			objectNode.put("cardAttacker", actionsInput.getCardAttacker().toObjectNode());
			objectNode.put("cardAttacked", actionsInput.getCardAttacked().toObjectNode());
			objectNode.put("error", "Attacker card has already attacked this turn.");
			return objectNode;
		} else if (attackingStatus == AttackingStatus.ATTACKING_STATUS_NOT_ENEMY) {
			objectNode.put("cardAttacker", actionsInput.getCardAttacker().toObjectNode());
			objectNode.put("cardAttacked", actionsInput.getCardAttacked().toObjectNode());
			objectNode.put("error", "Attacked card does not belong to the enemy.");
			return objectNode;
		} else if (attackingStatus == AttackingStatus.ATTACKING_STATUS_FROZEN) {
			objectNode.put("cardAttacker", actionsInput.getCardAttacker().toObjectNode());
			objectNode.put("cardAttacked", actionsInput.getCardAttacked().toObjectNode());
			objectNode.put("error", "Attacker card is frozen.");
			return objectNode;
		} else if (attackingStatus == AttackingStatus.ATTACKING_STATUS_NOT_TANK) {
			objectNode.put("cardAttacker", actionsInput.getCardAttacker().toObjectNode());
			objectNode.put("cardAttacked", actionsInput.getCardAttacked().toObjectNode());
			objectNode.put("error", "Attacked card is not of type 'Tank'.");
			return objectNode;
		}

		return null;
	}
}
