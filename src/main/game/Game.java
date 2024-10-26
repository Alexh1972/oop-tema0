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

import static java.lang.System.exit;

public class Game {
	private static Player firstPlayer;
	private static Player secondPlayer;

	private int roundMana = 2;
	private static int playerTurn;
	private boolean firstTurnEnded;
	private static ArrayList<GameCharacter>[] board;
	private static int playerWon = 0;
	ObjectMapper objectMapper = new ObjectMapper();
	private static int totalGames;
	private static int playerTwoWins;
	private static int playerOneWins;
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
		playerWon = 0;

		if (gameNo == 0) {
			totalGames = 0;
			playerTwoWins = 0;
			playerOneWins = 0;
		}
	}

	public ObjectNode handleAction(ActionsInput actionsInput) {
		Action action = Action.toAction(actionsInput, this);

		if (action != null)
			return action.execute();

		return null;
	}

	public void printBoard() {
		int i = 0;
		for (ArrayList<GameCharacter> arrayList : board) {
			System.out.println(i + ": ");
			for (GameCharacter gameCharacter : arrayList) {
				System.out.println(gameCharacter);
			}
			i++;
			System.out.println();
		}
		System.out.println();
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

	public static void won(int player) {
		playerWon = player;
		if (player == 1)
			playerOneWins++;
		else
			playerTwoWins++;
		totalGames++;
	}

	public AttackingStatus attackAgainstHero(Coordinates attacker) {
		AttackingStatus attackingStatus = isValidAttack(attacker, true);
		if (!attackingStatus.equals(AttackingStatus.ATTACKING_STATUS_SUCCESS))
			return attackingStatus;

		int playerAttacker = attacker.getX() > 1 ? 1 : 2;
		GameCharacter attackerCharacter = board[attacker.getX()].get(attacker.getY());
		Player enemy = getEnemyPlayer(playerAttacker);
		int heroHealth = enemy.getHero().getCard().getHealth();

		heroHealth -= attackerCharacter.getCard().getAttackDamage();
		enemy.getHero().getCard().setHealth(heroHealth);

		if (heroHealth <= 0) {
			won(playerAttacker);
		}

		return AttackingStatus.ATTACKING_STATUS_SUCCESS;
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
		if ( board[attacked.getX()].size() <= attacked.getY())
			return AttackingStatus.ATTACKING_STATUS_NOT_ENEMY;
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

	public static boolean hasManaForHero(int playerNo) {
		Player player = getPlayer(playerNo);

		if (player != null) {
			if (player.getHero().getCard().getMana() <= player.getMana())
				return true;
		}

		return false;
	}

	public void subtractManaFromPlayer(int mana, int playerNo) {
		Player player = getPlayer(playerNo);

		if (player != null) {
			player.subtractMana(mana);
		}
	}

	public static AttackingStatus isValidAttack(Coordinates attacker, boolean toEnemy) {
		int playerAttacker = attacker.getX() > 1 ? 1 : 2;
		if ( board[attacker.getX()].size() <= attacker.getY())
			return AttackingStatus.ATTACKING_STATUS_NOT_ENEMY;
		GameCharacter attackerCharacter = board[attacker.getX()].get(attacker.getY());

		if (attackerCharacter.isAttackedTurn())
			return AttackingStatus.ATTACKING_STATUS_ALREADY_ATTACKED;

		if (attackerCharacter.isFrozen())
			return AttackingStatus.ATTACKING_STATUS_FROZEN;

		int begIndex = 2 * playerAttacker - 2;
		boolean validAttacking = true;
		for (int i = 0; i <= 1 && validAttacking && toEnemy; i++) {
			for (int j = 0; j < board[i + begIndex].size() && validAttacking; j++) {
				if (board[i + begIndex].get(j).isTank())
					validAttacking = false;
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

	public static Player getPlayer(int idx) {
		if (idx == 1)
			return firstPlayer;
		if (idx == 2)
			return secondPlayer;

		return null;
	}

	public static Player getEnemyPlayer(int idx) {
		if (idx == 1)
			return secondPlayer;

		if (idx == 2)
			return firstPlayer;

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

	public ArrayNode frozenCardsToArrayNode() {
		ArrayNode arrayNode = objectMapper.createArrayNode();

		for (int i = 0; i < board.length; i++) {
			for (GameCharacter gameCharacter : board[i]) {
				if (gameCharacter.isFrozen())
					arrayNode.add(gameCharacter.getCard().toObjectNode(false));
			}
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

	public static int getPlayerTurn() {
		return playerTurn;
	}

	public static int getPlayerWon() {
		return playerWon;
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

	public static ArrayList<GameCharacter>[] getBoard() {
		return board;
	}

	public void setBoard(ArrayList<GameCharacter>[] board) {
		this.board = board;
	}

	public Player getSecondPlayer() {
		return secondPlayer;
	}

	public static void setFirstPlayer(Player firstPlayer) {
		Game.firstPlayer = firstPlayer;
	}

	public static void setSecondPlayer(Player secondPlayer) {
		Game.secondPlayer = secondPlayer;
	}

	public static void setPlayerWon(int playerWon) {
		Game.playerWon = playerWon;
	}

	public ObjectMapper getObjectMapper() {
		return objectMapper;
	}

	public void setObjectMapper(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
	}

	public static int getTotalGames() {
		return totalGames;
	}

	public static void setTotalGames(int totalGames) {
		Game.totalGames = totalGames;
	}

	public static int getPlayerTwoWins() {
		return playerTwoWins;
	}

	public static void setPlayerTwoWins(int playerTwoWins) {
		Game.playerTwoWins = playerTwoWins;
	}

	public static int getPlayerOneWins() {
		return playerOneWins;
	}

	public static void setPlayerOneWins(int playerOneWins) {
		Game.playerOneWins = playerOneWins;
	}
}

