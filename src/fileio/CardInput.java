package fileio;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import main.game.Deck;

import java.util.ArrayList;

public final class CardInput {
    private int mana;
    private int attackDamage;
    private int health;
    private String description;
    private ArrayList<String> colors;
    private String name;

    ObjectMapper objectMapper = new ObjectMapper();

    public CardInput() {
    }

    public ObjectNode toObjectNode(boolean isHero) {
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("mana", mana);

        if (!isHero)
            objectNode.put("attackDamage", attackDamage);

        objectNode.put("health", health);
        objectNode.put("description", description);

        ArrayNode arrayNode = objectMapper.createArrayNode();
        for (String color : colors) {
            arrayNode.add(color);
        }
        objectNode.put("colors", arrayNode);
        objectNode.put("name", name);

        return objectNode;
    }

    public CardInput deepCopy() {
        CardInput cardInput = new CardInput();
        cardInput.setAttackDamage(attackDamage);
        cardInput.setHealth(health);
        cardInput.setDescription(description);
        cardInput.setName(name);
        cardInput.setMana(mana);
        cardInput.setColors(colors);
        return cardInput;
    }

    public int getMana() {
        return mana;
    }

    public void setMana(final int mana) {
        this.mana = mana;
    }

    public int getAttackDamage() {
        return attackDamage;
    }

    public void setAttackDamage(final int attackDamage) {
        this.attackDamage = attackDamage;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(final int health) {
        this.health = health;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public ArrayList<String> getColors() {
        return colors;
    }

    public void setColors(final ArrayList<String> colors) {
        this.colors = colors;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "CardInput{"
                +  "mana="
                + mana
                +  ", attackDamage="
                + attackDamage
                + ", health="
                + health
                +  ", description='"
                + description
                + '\''
                + ", colors="
                + colors
                + ", name='"
                +  ""
                + name
                + '\''
                + '}';
    }
}
