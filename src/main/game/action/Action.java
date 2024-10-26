package main.game.action;

import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.ActionsInput;

public class Action {
	ActionsInput actionsInput;
	public Action(ActionsInput actionsInput) {

	}
	public ObjectNode execute() {
		return null;
	}

	public static Action toAction(ActionsInput actionsInput) {
		switch (actionsInput.getCommand()) {
			case "getPlayerDeck":
				return new GetPlayerDeck(actionsInput);
			case "getCardsInHand":
				return new GetCardsInHand(actionsInput);
			case "getPlayerTurn":
				return new GetPlayerTurn(actionsInput);
			case "getPlayerHero":
				return new GetPlayerHero(actionsInput);
			case "getPlayerMana":
				return new GetPlayerMana(actionsInput);
			case "endPlayerTurn":
				return new EndPlayerTurn(actionsInput);
			case "placeCard":
				return new PlaceCard(actionsInput);
			case "getCardsOnTable":
				return new GetCardsOnTable(actionsInput);
			case "cardUsesAttack":
				return new CardAttack(actionsInput);
			case "getCardAtPosition":
				return new GetCardAtPosition(actionsInput);
		}
		return null;
	}
}
