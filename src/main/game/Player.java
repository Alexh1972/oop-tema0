package main.game;

import fileio.CardInput;

import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Player {
	private final CardInput hero;
	private Deck deck;
	private Deck hand;
	private int mana = 0;
	public Player(CardInput hero, List<CardInput> deck, int shuffleSeed) {
		this.hero = hero;
		this.hero.setHealth(30);
		Collections.shuffle(deck, new Random(shuffleSeed));

		CardInput firstRoundCard = deck.remove(0);
		this.deck = new Deck(deck);
		this.hand = new Deck(firstRoundCard);
	}

	public void addMana(int addition) {
		mana += addition;
	}

	public void subtractMana(int subtraction) {
		mana -= subtraction;
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
