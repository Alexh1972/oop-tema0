package main.game.character;

import fileio.CardInput;
import fileio.Coordinates;
import main.game.Game;
import main.game.action.AttackingStatus;

public class TheRipper extends GameCharacter {
	public TheRipper(CardInput card) {
		super(card, false, true);
	}
	@Override
	public int boardPlacementIndex(int noPlayer) {
		return 3 - noPlayer;
	}

	@Override
	public AttackingStatus cardAbility(Coordinates attacker, Coordinates attacked) {
		GameCharacter attackedCharacter = Game.getCard(attacked.getX(), attacked.getY());

		if (attackedCharacter != null) {
			int damage = attackedCharacter.card.getAttackDamage();
			damage -= 2;
			if (damage < 0)
				damage = 0;
			attackedCharacter.card.setAttackDamage(damage);
		}

		return AttackingStatus.ATTACKING_STATUS_SUCCESS;
	}
}
