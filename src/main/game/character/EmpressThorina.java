package main.game.character;

import fileio.CardInput;
import main.game.Game;
import main.game.action.AttackingStatus;

import java.util.ArrayList;

public class EmpressThorina extends GameCharacter {
	public EmpressThorina(CardInput card) {
		super(card, false, true);
	}

	@Override
	public AttackingStatus heroAbility(int player, int row) {
		int playerAttacked = (5 - row) / 2;

		if (player == playerAttacked)
			return AttackingStatus.ATTACKING_STATUS_NOT_ENEMY;

		ArrayList<GameCharacter>[] board = Game.getBoard();
		int maximum = -1, index = -1;

		for (int i = 0; i < board[row].size(); i++) {
			GameCharacter gameCharacter = board[row].get(i);
			if (gameCharacter.getCard().getHealth() > maximum) {
				maximum = gameCharacter.getCard().getHealth();
				index = i;
			}
		}

		if (index != -1)
			board[row].remove(index);

		return AttackingStatus.ATTACKING_STATUS_SUCCESS;
	}
}
