package main.game.action;

import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.ActionsInput;
import main.game.Game;
import main.game.Player;
import main.game.character.GameCharacter;

public class PlaceCard extends Action {
	public PlaceCard(ActionsInput actionsInput, Game game) {
		super(actionsInput, game);
	}

	@Override
	public ObjectNode execute() {
		if (Game.getPlayerWon() != 0)
			return null;
		ObjectNode objectNode = objectMapper.createObjectNode();

		objectNode.put("command", actionsInput.getCommand());
		Player playerPlaceCard = game.getPlayer(game.getPlayerTurn());

		if (playerPlaceCard != null) {
			GameCharacter placedCharacter = playerPlaceCard.getCardFromHand(actionsInput.getHandIdx());

			if (placedCharacter == null) {
				objectNode.put("error", "Not enough mana to place card on table.");
				objectNode.put("handIdx", actionsInput.getHandIdx());
				return objectNode;
			}

			int index = placedCharacter.boardPlacementIndex(game.getPlayerTurn());

			if (!game.addCharacterToBoard(placedCharacter, index)) {
				objectNode.put("error", "Cannot place card on table since row is full.");
				objectNode.put("handIdx", actionsInput.getHandIdx());
				return objectNode;
			}

			playerPlaceCard.removeCardFromHand(actionsInput.getHandIdx());
		}

		return null;
	}
}
