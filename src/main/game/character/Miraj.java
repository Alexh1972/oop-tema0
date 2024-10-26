package main.game.character;

import fileio.CardInput;

public class Miraj extends GameCharacter {
	public Miraj(CardInput card) {
		super(card);
	}
	@Override
	public int boardPlacementIndex(int noPlayer) {
		return 3 - noPlayer;
	}
}
