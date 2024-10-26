package main.game.character;

import fileio.CardInput;
import main.game.Game;
import main.game.action.AttackingStatus;

import java.util.ArrayList;

public class KingMudface extends GameCharacter {
	public KingMudface(CardInput card) {
		super(card, false, false);
	}

	@Override
	public AttackingStatus heroAbility(int player, int row) {
		int playerAttacked = (5 - row) / 2;

		if (player != playerAttacked)
			return AttackingStatus.ATTACKING_STATUS_ROW_NOT_ALLY;

		ArrayList<GameCharacter>[] board = Game.getBoard();
		for (GameCharacter character : board[row]) {
			int health = character.getCard().getHealth();
			character.getCard().setHealth(health + 1);
		}

		return AttackingStatus.ATTACKING_STATUS_SUCCESS;
	}
}
