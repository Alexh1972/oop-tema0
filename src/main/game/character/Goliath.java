package main.game.character;

import fileio.CardInput;

public class Goliath extends GameCharacter {
	public Goliath(CardInput card) {
		super(card, true);
	}

	public int boardPlacementIndex(int noPlayer) {
		return 3 - noPlayer;
	}
}
