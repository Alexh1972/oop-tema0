package main.game;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.ActionsInput;
import fileio.Coordinates;
import fileio.GameInput;
import fileio.Input;
import main.game.character.GameCharacter;

import java.util.ArrayList;
import java.util.List;

public class Game {
	private final Player firstPlayer;
	private final Player secondPlayer;

	private int roundMana = 2;
	private int playerTurn;
	private boolean firstTurnEnded;
	private ArrayList<GameCharacter>[] board;
	ObjectMapper objectMapper = new ObjectMapper();
	public Game(Input input, int gameNo) {
		GameInput gameInput = input.getGames().get(gameNo);

		firstPlayer = new Player(gameInput.getStartGame()
										  .getPlayerOneHero(),
								 input.getPlayerOneDecks()
										 .getDecks()
										 .get(gameInput.getStartGame().getPlayerOneDeckIdx()),
								 gameInput.getStartGame().getShuffleSeed());
		secondPlayer = new Player(gameInput.getStartGame().getPlayerTwoHero(),
								 input.getPlayerTwoDecks()
										 .getDecks()
										 .get(gameInput.getStartGame().getPlayerTwoDeckIdx()),
								 gameInput.getStartGame().getShuffleSeed());
		playerTurn = gameInput.getStartGame().getStartingPlayer();
		board = new ArrayList[4];
		for (int i = 0 ; i < board.length; i++)
			board[i] = new ArrayList<>();
	}

	public ObjectNode handleAction(ActionsInput actionsInput) {
		ObjectNode objectNode = objectMapper.createObjectNode();

		objectNode.put("command", actionsInput.getCommand());

		switch (actionsInput.getCommand()) {
			case "getPlayerDeck":
				objectNode.put("playerIdx", actionsInput.getPlayerIdx());
				Player playerDeck = getPlayer(actionsInput.getPlayerIdx());

				if (playerDeck != null) {
					objectNode.put("output", playerDeck.getDeck().toArrayNode());
					return objectNode;
				}
				break;
			case "getCardsInHand":
				objectNode.put("playerIdx", actionsInput.getPlayerIdx());
				Player playerHand = getPlayer(actionsInput.getPlayerIdx());

				if (playerHand != null) {
					objectNode.put("output", playerHand.getHand().toArrayNode());
					return objectNode;
				}
				break;
			case "getPlayerTurn":
				objectNode.put("output", playerTurn);
				return objectNode;
			case "getPlayerHero":
				objectNode.put("playerIdx", actionsInput.getPlayerIdx());
				Player playerHero = getPlayer(actionsInput.getPlayerIdx());

				if (playerHero != null) {
					objectNode.put("output", playerHero.getHero().getCard().toObjectNode(true));
					return objectNode;
				}
				break;
			case "getPlayerMana":
				objectNode.put("playerIdx", actionsInput.getPlayerIdx());
				Player playerMana = getPlayer(actionsInput.getPlayerIdx());

				if (playerMana != null) {
					objectNode.put("output", playerMana.getMana());
					return objectNode;
				}
			case "endPlayerTurn":
				if (firstTurnEnded) {
					firstPlayer.addMana(roundMana);
					secondPlayer.addMana(roundMana);
					incrementRoundMana();
					firstPlayer.takeCardInHand();
					secondPlayer.takeCardInHand();
				}

				firstTurnEnded = !firstTurnEnded;
				playerTurn = 3 - playerTurn;
				break;
			case "placeCard":
				Player playerPlaceCard = getPlayer(playerTurn);

				if (playerPlaceCard != null) {
					GameCharacter placedCharacter = playerPlaceCard.placeCardFromHand(actionsInput.getHandIdx());

					if (placedCharacter == null) {
						objectNode.put("error", "Not enough mana to place card on table.");
						objectNode.put("handIdx", actionsInput.getHandIdx());
						return objectNode;
					}

					int index = placedCharacter.boardPlacementIndex(playerTurn);

					if (!addCharacterToBoard(placedCharacter, index)) {
						objectNode.put("error", "Cannot place card on table since row is full.");
						objectNode.put("handIdx", actionsInput.getHandIdx());
						return objectNode;
					}
				}
				break;
			case "getCardsOnTable":
				objectNode.put("output", boardToArrayNode());
				return objectNode;
			case "cardUsesAttack":
				AttackingStatus attackingStatus = attackOnBoard(actionsInput.getCardAttacker(), actionsInput.getCardAttacked());
				if (attackingStatus == AttackingStatus.ATTACKING_STATUS_ALREADY_ATTACKED) {
					objectNode.put("cardAttacker", actionsInput.getCardAttacker().toObjectNode());
					objectNode.put("cardAttacked", actionsInput.getCardAttacked().toObjectNode());
					objectNode.put("error", "Attacker card has already attacked this turn.");
				} else if (attackingStatus == AttackingStatus.ATTACKING_STATUS_NOT_ENEMY) {
					objectNode.put("cardAttacker", actionsInput.getCardAttacker().toObjectNode());
					objectNode.put("cardAttacked", actionsInput.getCardAttacked().toObjectNode());
					objectNode.put("error", "Attacked card does not belong to the enemy.");
				} else if (attackingStatus == AttackingStatus.ATTACKING_STATUS_FROZEN) {
					objectNode.put("cardAttacker", actionsInput.getCardAttacker().toObjectNode());
					objectNode.put("cardAttacked", actionsInput.getCardAttacked().toObjectNode());
					objectNode.put("error", "Attacker card is frozen.");
				} else if (attackingStatus == AttackingStatus.ATTACKING_STATUS_NOT_TANK) {
					objectNode.put("cardAttacker", actionsInput.getCardAttacker().toObjectNode());
					objectNode.put("cardAttacked", actionsInput.getCardAttacked().toObjectNode());
					objectNode.put("error", "Attacked card is not of type 'Tank'.");
				}
				break;
			case "getCardAtPosition":
				GameCharacter cardAtPosition = getCard(actionsInput.getX(), actionsInput.getY());

				objectNode.put("x", actionsInput.getX());
				objectNode.put("y", actionsInput.getY());
				if (cardAtPosition == null) {
					objectNode.put("output", "No card available at that position.");
				} else {
					objectNode.put("output", cardAtPosition.getCard().toObjectNode(false));
				}
				return objectNode;

		}

		return null;
	}

	private GameCharacter getCard(int x, int y) {
		if (x >= board.length)
			return null;

		if (y >= board[x].size())
			return null;

		return board[x].get(y);
	}
	private AttackingStatus attackOnBoard(Coordinates attacker, Coordinates attacked) {
		int playerAttacker = attacker.getX() > 1 ? 1 : 2;
		int playerAttacked = attacked.getX() > 1 ? 1 : 2;

		if (playerAttacked == playerAttacker)
			return AttackingStatus.ATTACKING_STATUS_NOT_ENEMY;

		GameCharacter attackerCharacter = board[attacker.getX()].get(attacker.getY());

		if (attackerCharacter.isAttackedTurn())
			return AttackingStatus.ATTACKING_STATUS_ALREADY_ATTACKED;

		if (attackerCharacter.isFrozen())
			return AttackingStatus.ATTACKING_STATUS_FROZEN;
		GameCharacter attackedCharacter = board[attacked.getX()].get(attacked.getY());

		int begIndex = (2 - playerAttacked) * 2;
		boolean validAttacking = true;
		for (int i = 0; i <= 1 && !attackedCharacter.isTank() && validAttacking; i++) {
			for (int j = 0; j < board[i + begIndex].size() && validAttacking; j++) {
				if (i + begIndex != attacked.getX() || j != attacked.getY()) {
					if (board[i + begIndex].get(j).isTank())
						validAttacking = false;
				}
			}
		}

		if (!validAttacking)
			return AttackingStatus.ATTACKING_STATUS_NOT_TANK;

		int newHealth = attackedCharacter.getCard().getHealth() - attackerCharacter.getCard().getAttackDamage();

		if (newHealth <= 0) {
			board[attacked.getX()].remove(attacked.getY());
		} else {
			attackedCharacter.getCard().setHealth(newHealth);
		}
		return AttackingStatus.ATTACKING_STATUS_SUCCESS;
	}

	private void incrementRoundMana() {
		if (roundMana == 10)
			return;

		roundMana++;
	}

	public Player getPlayer(int idx) {
		if (idx == 1)
			return firstPlayer;
		if (idx == 2)
			return secondPlayer;

		return null;
	}

	public ArrayNode boardToArrayNode() {
		ArrayNode arrayNode = objectMapper.createArrayNode();

		for (int i = 0; i < board.length; i++) {
			ArrayNode arrayNodeRow = objectMapper.createArrayNode();
			for (GameCharacter gameCharacter : board[i]) {
				arrayNodeRow.add(gameCharacter.getCard().toObjectNode(false));
			}
			arrayNode.add(arrayNodeRow);
		}

		return arrayNode;
	}

	public boolean addCharacterToBoard(GameCharacter gameCharacter, int index) {
		if (board[index].size() < 5) {
			board[index].add(gameCharacter);
			return true;
		}

		return false;
	}

	@Override
	public String toString() {
		return "Game{" +
				"firstPlayer=" + firstPlayer +
				", secondPlayer=" + secondPlayer +
				'}';
	}

	public Player getFirstPlayer() {
		return firstPlayer;
	}

	public Player getSecondPlayer() {
		return secondPlayer;
	}

}

enum AttackingStatus {
	ATTACKING_STATUS_NOT_ENEMY,
	ATTACKING_STATUS_ALREADY_ATTACKED,
	ATTACKING_STATUS_SUCCESS,
	ATTACKING_STATUS_FROZEN,
	ATTACKING_STATUS_NOT_TANK
}
