package main.game.character;

import fileio.CardInput;

public class Warden extends GameCharacter {
	public Warden(CardInput card) {
		super(card, true);
	}

	public int boardPlacementIndex(int noPlayer) {
		return 3 - noPlayer;
	}
}
