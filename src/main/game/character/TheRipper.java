package main.game.character;

import fileio.CardInput;

public class TheRipper extends GameCharacter {
	public TheRipper(CardInput card) {
		super(card);
	}

	public int boardPlacementIndex(int noPlayer) {
		return 3 - noPlayer;
	}
}
