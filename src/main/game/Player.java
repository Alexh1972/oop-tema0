package main.game;

import fileio.CardInput;

import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Player {
	private final CardInput hero;
	private final Deck deck;
	public Player(CardInput hero, List<CardInput> deck, int shuffleSeed) {
		this.hero = hero;
		Collections.shuffle(deck, new Random(shuffleSeed));
		this.deck = new Deck(deck);
	}

	@Override
	public String toString() {
		return "Player{" +
				"hero=" + hero +
				", deck=" + deck +
				'}';
	}

	public CardInput getHero() {
		return hero;
	}

	public Deck getDeck() {
		return deck;
	}
}
