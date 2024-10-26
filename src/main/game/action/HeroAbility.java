package main.game.action;

import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.ActionsInput;
import main.game.Game;

public class HeroAbility extends Action {

	public HeroAbility(ActionsInput actionsInput, Game game) {
		super(actionsInput, game);
	}

	@Override
	public ObjectNode execute() {
		ObjectNode objectNode = objectMapper.createObjectNode();
		int player = Game.getPlayerTurn();

		objectNode.put("command", actionsInput.getCommand());
		objectNode.put("affectedRow", actionsInput.getAffectedRow());

		if (!Game.hasManaForHero(player)) {
			objectNode.put("error", "Not enough mana to use hero's ability.");
			return objectNode;
		}

		if (Game.getPlayer(player).getHero().isAttackedTurn()) {
			objectNode.put("error", "Hero has already attacked this turn.");
			return objectNode;
		}

		AttackingStatus attackingStatus = Game.getPlayer(player).getHero().heroAbility(player, actionsInput.getAffectedRow());

		if (attackingStatus.equals(AttackingStatus.ATTACKING_STATUS_ROW_NOT_ALLY)) {
			objectNode.put("error", "Selected row does not belong to the current player.");
			return objectNode;
		}

		if (attackingStatus.equals(AttackingStatus.ATTACKING_STATUS_ROW_NOT_ENEMY)) {
			objectNode.put("error", "Selected row does not belong to the enemy.");
			return objectNode;
		}

		game.subtractManaFromPlayer(Game.getPlayer(player).getHero().getCard().getMana(), player);
		return null;
	}
}
