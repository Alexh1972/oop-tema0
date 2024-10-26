package main.game.character;

import fileio.CardInput;
import fileio.Coordinates;
import main.game.Game;
import main.game.action.AttackingStatus;

public class Miraj extends GameCharacter {
	public Miraj(CardInput card) {
		super(card, false, true);
	}
	@Override
	public int boardPlacementIndex(int noPlayer) {
		return 3 - noPlayer;
	}

	@Override
	public AttackingStatus cardAbility(Coordinates attacker, Coordinates attacked) {
		GameCharacter attackedCharacter = Game.getCard(attacked.getX(), attacked.getY());
		GameCharacter attackerCharacter = Game.getCard(attacker.getX(), attacker.getY());

		if (attackedCharacter != null && attackerCharacter != null) {
			int healthAttacker = attackerCharacter.getCard().getHealth();
			int healthAttacked = attackedCharacter.getCard().getHealth();

			attackedCharacter.card.setHealth(healthAttacker);
			attackerCharacter.card.setHealth(healthAttacked);
		}

		return AttackingStatus.ATTACKING_STATUS_SUCCESS;
	}
}
