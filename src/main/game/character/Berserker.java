package main.game.character;

import fileio.CardInput;

public class Berserker extends GameCharacter {
	public Berserker(CardInput card) {
		super(card);
	}

	public int boardPlacementIndex(int noPlayer) {
		return (2 - noPlayer) * 3;
	}
}
