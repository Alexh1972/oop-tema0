package main.game.character;

import fileio.CardInput;
import fileio.Coordinates;
import main.game.Game;
import main.game.action.AttackingStatus;

public class Disciple extends GameCharacter {
	public Disciple(CardInput card) {
		super(card, false, false);
	}
	@Override
	public int boardPlacementIndex(int noPlayer) {
		return (2 - noPlayer) * 3;
	}

	@Override
	public AttackingStatus cardAbility(Coordinates attacker, Coordinates attacked) {
		GameCharacter attackedCharacter = Game.getCard(attacked.getX(), attacked.getY());

		if (attackedCharacter != null) {
			int health = attackedCharacter.card.getHealth();
			health += 2;

			attackedCharacter.card.setHealth(health);
		}

		return AttackingStatus.ATTACKING_STATUS_SUCCESS;
	}
}
