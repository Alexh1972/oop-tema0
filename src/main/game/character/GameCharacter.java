package main.game.character;

import fileio.CardInput;
import fileio.Coordinates;
import main.game.action.AttackingStatus;

import java.util.ArrayList;
import java.util.List;

public class GameCharacter {
	CardInput card;
	boolean frozen;
	boolean attackedTurn;

	boolean tank;

	boolean abilityToEnemies;

	public GameCharacter(CardInput card, boolean tank, boolean abilityToEnemies) {
		this.card = card;
		this.tank = tank;
		this.abilityToEnemies = abilityToEnemies;
	}

	public static GameCharacter toGameCharacter(CardInput cardInput) {
		GameCharacter gameCharacter = null;

		switch (cardInput.getName()) {
			case "Sentinel":
				gameCharacter = new Sentinel(cardInput);
				break;
			case "Berserker":
				gameCharacter = new Berserker(cardInput);
				break;
			case "Goliath":
				gameCharacter = new Goliath(cardInput);
				break;
			case "Warden":
				gameCharacter = new Warden(cardInput);
				break;
			case "Disciple":
				gameCharacter = new Disciple(cardInput);
				break;
			case "The Ripper":
				gameCharacter = new TheRipper(cardInput);
				break;
			case "Miraj":
				gameCharacter = new Miraj(cardInput);
				break;
			case "The Cursed One":
				gameCharacter = new TheCursedOne(cardInput);
				break;
			case "Lord Royce":
				gameCharacter = new LordRoyce(cardInput);
				break;
			case "Empress Thorina":
				gameCharacter = new EmpressThorina(cardInput);
				break;
			case "King Mudface":
				gameCharacter = new KingMudface(cardInput);
				break;
			case "General Kocioraw":
				gameCharacter = new GeneralKocioraw(cardInput);
				break;
		}
		return gameCharacter;
	}

	public static List<GameCharacter> toGameCharacters(List<CardInput> cardInputList) {
		List<GameCharacter> gameCharacters = new ArrayList<>();

		for (CardInput cardInput : cardInputList) {
			gameCharacters.add(toGameCharacter(cardInput));
		}

		return gameCharacters;
	}

	public int boardPlacementIndex(int noPlayer) {
		return -1;
	}

	public AttackingStatus cardAbility(Coordinates attacker, Coordinates attacked) {
		return AttackingStatus.ATTACKING_STATUS_SUCCESS;
	}

	public CardInput getCard() {
		return card;
	}

	public boolean isFrozen() {
		return frozen;
	}

	public void setFrozen(boolean frozen) {
		this.frozen = frozen;
	}

	public boolean isAttackedTurn() {
		return attackedTurn;
	}

	public void setAttackedTurn(boolean attackedTurn) {
		this.attackedTurn = attackedTurn;
	}

	public boolean isTank() {
		return tank;
	}

	public void setTank(boolean tank) {
		this.tank = tank;
	}

	public void setCard(CardInput card) {
		this.card = card;
	}

	public boolean isAbilityToEnemies() {
		return abilityToEnemies;
	}

	public void setAbilityToEnemies(boolean abilityToEnemies) {
		this.abilityToEnemies = abilityToEnemies;
	}
}
