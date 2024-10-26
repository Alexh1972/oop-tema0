package main.game.action;

import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.ActionsInput;
import main.game.Game;
import main.game.character.GameCharacter;

import java.util.ArrayList;

public class EndPlayerTurn extends Action {
	public EndPlayerTurn(ActionsInput actionsInput, Game game) {
		super(actionsInput, game);
	}

	@Override
	public ObjectNode execute() {
		if (Game.getPlayerWon() != 0)
			return null;
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

		ArrayList<GameCharacter>[] board = Game.getBoard();
		for (ArrayList<GameCharacter> gameCharacters : board) {
			for (GameCharacter gameCharacter : gameCharacters) {
				gameCharacter.setFrozen(false);
				gameCharacter.setAttackedTurn(false);
			}
		}

		game.getFirstPlayer().getHero().setAttackedTurn(false);
		game.getSecondPlayer().getHero().setAttackedTurn(false);
		game.getFirstPlayer().getHero().setFrozen(false);
		game.getSecondPlayer().getHero().setFrozen(false);
		return null;
	}
}
