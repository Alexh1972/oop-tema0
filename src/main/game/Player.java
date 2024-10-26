package main.game;

import fileio.CardInput;
import main.game.character.GameCharacter;

import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Player {
	private final GameCharacter hero;
	private Deck deck;
	private Deck hand;
	private int mana = 1;
	public Player(CardInput hero, List<CardInput> deck, int shuffleSeed) {
		this.hero = GameCharacter.toGameCharacter(hero);
		this.hero.getCard().setHealth(30);

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

	public void takeCardInHand() {
		if (!deck.getCards().isEmpty()) {
			GameCharacter gameCharacter = deck.getCards().remove(0);
			hand.getCards().add(gameCharacter);
		}
	}

	public GameCharacter placeCardFromHand(int index) {
		if (index < hand.getCards().size()) {
			if (mana >= hand.getCards().get(index).getCard().getMana()) {
				GameCharacter gameCharacter = hand.getCards().remove(index);
				subtractMana(gameCharacter.getCard().getMana());
				return gameCharacter;
			}
		}
		return null;
	}

	@Override
	public String toString() {
		return "Player{" +
				"hero=" + hero +
				", deck=" + deck +
				'}';
	}

	public GameCharacter getHero() {
		return hero;
	}

	public Deck getHand() {
		return hand;
	}

	public Deck getDeck() {
		return deck;
	}

	public int getMana() {
		return mana;
	}
}
