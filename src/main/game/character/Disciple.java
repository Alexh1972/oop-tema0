package main.game.character;

import fileio.CardInput;

public class Disciple extends GameCharacter {
	public Disciple(CardInput card) {
		super(card);
	}

	public int boardPlacementIndex(int noPlayer) {
		return (2 - noPlayer) * 3;
	}
}
