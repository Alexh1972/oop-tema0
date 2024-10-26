package main.game.character;

import fileio.CardInput;
import main.game.Game;
import main.game.action.AttackingStatus;

import java.util.ArrayList;

public class LordRoyce extends GameCharacter {
	public LordRoyce(CardInput card) {
		super(card, false, true);
	}

	@Override
	public AttackingStatus heroAbility(int player, int row) {
		int playerAttacked = (5 - row) / 2;

		if (player == playerAttacked)
			return AttackingStatus.ATTACKING_STATUS_NOT_ENEMY;

		ArrayList<GameCharacter>[] board = Game.getBoard();
		for (GameCharacter character : board[row]) {
			character.setFrozen(true);
		}

		return AttackingStatus.ATTACKING_STATUS_SUCCESS;
	}
}
