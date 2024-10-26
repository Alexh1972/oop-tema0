package main.game;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import fileio.CardInput;
import main.game.character.GameCharacter;

import java.util.ArrayList;
import java.util.List;

public class Deck {
	private List<GameCharacter> cards;
	private ObjectMapper objectMapper = new ObjectMapper();

	public Deck(List<CardInput> cards) {
		this.cards = GameCharacter.toGameCharacters(cards);
	}
	public Deck(CardInput card) {
		this.cards = new ArrayList<>();
		this.cards.add(GameCharacter.toGameCharacter(card));
	}

	public List<GameCharacter> getCards() {
		return cards;
	}

	public ArrayNode toArrayNode() {
		ArrayNode arrayNode = objectMapper.createArrayNode();

		for (GameCharacter gameCharacter : cards) {
			arrayNode.add(gameCharacter.getCard().toObjectNode(false));
		}

		return arrayNode;
	}

	@Override
	public String toString() {
		return "Deck{" +
				"cards=" + cards +
				'}';
	}
}
