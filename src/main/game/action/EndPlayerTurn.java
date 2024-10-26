package main.game.action;

import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.ActionsInput;
import main.game.Game;

public class EndPlayerTurn extends Action {
	public EndPlayerTurn(ActionsInput actionsInput, Game game) {
		super(actionsInput, game);
	}

	@Override
	public ObjectNode execute() {
		boolean firstTurnEnded = game.isFirstTurnEnded();
		int playerTurn = game.getPlayerTurn();
		if (game.isFirstTurnEnded()) {
			game.getFirstPlayer().addMana(game.getRoundMana());
			game.getSecondPlayer().addMana(game.getRoundMana());
			game.incrementRoundMana();
			game.getFirstPlayer().takeCardInHand();
			game.getSecondPlayer().takeCardInHand();
		}


		firstTurnEnded = !firstTurnEnded;
		playerTurn = 3 - playerTurn;
		game.setFirstTurnEnded(firstTurnEnded);
		game.setPlayerTurn(playerTurn);

		return null;
	}
}
