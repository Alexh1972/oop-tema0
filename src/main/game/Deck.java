package main.game;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import fileio.CardInput;

import java.util.ArrayList;
import java.util.List;

public class Deck {
	List<CardInput> cards;
	ObjectMapper objectMapper = new ObjectMapper();

	public Deck(List<CardInput> cards) {
		this.cards = cards;
	}
	public Deck(CardInput card) {
		this.cards = new ArrayList<>();
		this.cards.add(card);
	}

	public List<CardInput> getCards() {
		return cards;
	}

	public ArrayNode toArrayNode() {
		ArrayNode arrayNode = objectMapper.createArrayNode();

		for (CardInput cardInput : cards) {
			arrayNode.add(cardInput.toObjectNode(false));
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
