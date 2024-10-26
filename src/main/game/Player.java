package main.game;

import fileio.CardInput;
import main.game.character.GameCharacter;

import java.util.ArrayList;
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

		List<CardInput> copyDeck = new ArrayList<>();
		copyDeck.addAll(deck.stream().map(CardInput::deepCopy).toList());
		Collections.shuffle(copyDeck, new Random(shuffleSeed));
		CardInput firstRoundCard = copyDeck.remove(0);
		this.deck = new Deck(copyDeck);
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

	public GameCharacter getCard(int index) {
		return hand.getCards().get(index);
	}

	public GameCharacter getCardFromHand(int index) {
		if (index < hand.getCards().size()) {
			if (mana >= hand.getCards().get(index).getCard().getMana()) {
				return hand.getCards().get(index);
			}
		}
		return null;
	}

	public GameCharacter removeCardFromHand(int index) {
		GameCharacter gameCharacter = hand.getCards().remove(index);
		subtractMana(gameCharacter.getCard().getMana());
		return gameCharacter;
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
