package main.game.character;

import fileio.CardInput;
import fileio.Coordinates;
import main.game.Game;
import main.game.action.AttackingStatus;

public class TheCursedOne extends GameCharacter {
	public TheCursedOne(CardInput card) {
		super(card, false, true);
	}

	public int boardPlacementIndex(int noPlayer) {
		return (2 - noPlayer) * 3;
	}

	@Override
	public AttackingStatus cardAbility(Coordinates attacker, Coordinates attacked) {
		GameCharacter attackedCharacter = Game.getCard(attacked.getX(), attacked.getY());

		if (attackedCharacter != null) {
			int attack = attackedCharacter.card.getAttackDamage();
			int health = attackedCharacter.card.getHealth();

			attackedCharacter.card.setAttackDamage(health);
			attackedCharacter.card.setHealth(attack);

			if (attackedCharacter.card.getHealth() <= 0)
				Game.removeCard(attacked.getX(), attacked.getY());
		}

		return AttackingStatus.ATTACKING_STATUS_SUCCESS;
	}
}
