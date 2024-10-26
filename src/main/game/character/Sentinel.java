package main.game.character;

import fileio.CardInput;

public class Sentinel extends GameCharacter {
	public Sentinel(CardInput card) {
		super(card);
	}
	@Override
	public int boardPlacementIndex(int noPlayer) {
		return (2 - noPlayer) * 3;
	}
}
