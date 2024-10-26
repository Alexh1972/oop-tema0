package main.game.action;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.ActionsInput;
import main.game.Game;

public class Action {
	protected ActionsInput actionsInput;
	protected ObjectMapper objectMapper = new ObjectMapper();
	protected Game game;
	public Action(ActionsInput actionsInput, Game game) {
		this.actionsInput = actionsInput;
		this.game = game;
	}
	public ObjectNode execute() {
		return null;
	}

	public static Action toAction(ActionsInput actionsInput, Game game) {
		switch (actionsInput.getCommand()) {
			case "getPlayerDeck":
				return new GetPlayerDeck(actionsInput, game);
			case "getCardsInHand":
				return new GetCardsInHand(actionsInput, game);
			case "getPlayerTurn":
				return new GetPlayerTurn(actionsInput, game);
			case "getPlayerHero":
				return new GetPlayerHero(actionsInput, game);
			case "getPlayerMana":
				return new GetPlayerMana(actionsInput, game);
			case "endPlayerTurn":
				return new EndPlayerTurn(actionsInput, game);
			case "placeCard":
				return new PlaceCard(actionsInput, game);
			case "getCardsOnTable":
				return new GetCardsOnTable(actionsInput, game);
			case "cardUsesAttack":
				return new CardAttack(actionsInput, game);
			case "getCardAtPosition":
				return new GetCardAtPosition(actionsInput, game);
			case "cardUsesAbility":
				return new CardAbility(actionsInput, game);
			case "useAttackHero":
				return new AttackAgainstHero(actionsInput, game);
			case "useHeroAbility":
				return new HeroAbility(actionsInput, game);
			case "getFrozenCardsOnTable":
				return new GetFrozenCards(actionsInput, game);
		}
		return null;
	}
}
