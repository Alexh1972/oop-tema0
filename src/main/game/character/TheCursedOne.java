package main.game.character;

import fileio.CardInput;

public class TheCursedOne extends GameCharacter {
	public TheCursedOne(CardInput card) {
		super(card);
	}

	public int boardPlacementIndex(int noPlayer) {
		return (2 - noPlayer) * 3;
	}
}
