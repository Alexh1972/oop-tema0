package main.game;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.ActionsInput;
import fileio.Coordinates;
import fileio.GameInput;
import fileio.Input;
import main.game.action.Action;
import main.game.action.AttackingStatus;
import main.game.character.GameCharacter;

import java.util.ArrayList;
import java.util.List;

public class Game {
	private final Player firstPlayer;
	private final Player secondPlayer;

	private int roundMana = 2;
	private int playerTurn;
	private boolean firstTurnEnded;
	private static ArrayList<GameCharacter>[] board;
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
		Action action = Action.toAction(actionsInput, this);

		if (action != null)
			return action.execute();

		return null;
	}

	public static void removeCard(int x, int y) {
		if (x >= board.length)
			return;

		if (y >= board[x].size())
			return;

		board[x].remove(y);
	}

	public static GameCharacter getCard(int x, int y) {
		if (x >= board.length)
			return null;

		if (y >= board[x].size())
			return null;

		return board[x].get(y);
	}
	public AttackingStatus attackOnBoard(Coordinates attacker, Coordinates attacked) {
		AttackingStatus attackingStatus = isValidAttack(attacker, attacked, true);
		if (!attackingStatus.equals(AttackingStatus.ATTACKING_STATUS_SUCCESS))
			return attackingStatus;

		GameCharacter attackerCharacter = board[attacker.getX()].get(attacker.getY());
		GameCharacter attackedCharacter = board[attacked.getX()].get(attacked.getY());
		int newHealth = attackedCharacter.getCard().getHealth() - attackerCharacter.getCard().getAttackDamage();

		if (newHealth <= 0) {
			removeCard(attacked.getX(), attacked.getY());
		} else {
			attackedCharacter.getCard().setHealth(newHealth);
		}

		board[attacker.getX()].get(attacker.getY()).setAttackedTurn(true);
		return AttackingStatus.ATTACKING_STATUS_SUCCESS;
	}

	public static AttackingStatus isValidAttack(Coordinates attacker, Coordinates attacked, boolean toEnemy) {
		int playerAttacker = attacker.getX() > 1 ? 1 : 2;
		int playerAttacked = attacked.getX() > 1 ? 1 : 2;

		if (playerAttacked == playerAttacker && toEnemy)
			return AttackingStatus.ATTACKING_STATUS_NOT_ENEMY;

		if (playerAttacked != playerAttacker && !toEnemy)
			return AttackingStatus.ATTACKING_STATUS_NOT_ALLY;

		GameCharacter attackerCharacter = board[attacker.getX()].get(attacker.getY());

		if (attackerCharacter.isAttackedTurn())
			return AttackingStatus.ATTACKING_STATUS_ALREADY_ATTACKED;

		if (attackerCharacter.isFrozen())
			return AttackingStatus.ATTACKING_STATUS_FROZEN;
		GameCharacter attackedCharacter = board[attacked.getX()].get(attacked.getY());

		int begIndex = (2 - playerAttacked) * 2;
		boolean validAttacking = true;
		for (int i = 0; i <= 1 && !attackedCharacter.isTank() && validAttacking && toEnemy; i++) {
			for (int j = 0; j < board[i + begIndex].size() && validAttacking; j++) {
				if (i + begIndex != attacked.getX() || j != attacked.getY()) {
					if (board[i + begIndex].get(j).isTank())
						validAttacking = false;
				}
			}
		}

		if (!validAttacking)
			return AttackingStatus.ATTACKING_STATUS_NOT_TANK;

		return AttackingStatus.ATTACKING_STATUS_SUCCESS;
	}

	public void incrementRoundMana() {
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

	public int getRoundMana() {
		return roundMana;
	}

	public void setRoundMana(int roundMana) {
		this.roundMana = roundMana;
	}

	public int getPlayerTurn() {
		return playerTurn;
	}

	public void setPlayerTurn(int playerTurn) {
		this.playerTurn = playerTurn;
	}

	public boolean isFirstTurnEnded() {
		return firstTurnEnded;
	}

	public void setFirstTurnEnded(boolean firstTurnEnded) {
		this.firstTurnEnded = firstTurnEnded;
	}

	public ArrayList<GameCharacter>[] getBoard() {
		return board;
	}

	public void setBoard(ArrayList<GameCharacter>[] board) {
		this.board = board;
	}

	public Player getSecondPlayer() {
		return secondPlayer;
	}

}

