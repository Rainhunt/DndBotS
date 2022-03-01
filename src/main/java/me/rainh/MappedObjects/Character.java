package me.rainh.MappedObjects;

import me.rainh.Interfaces.Reference;
import me.rainh.MappedObjects.References.*;

import java.util.ArrayList;
import java.util.HashMap;

public class Character implements Reference {
    //fields
    String characterKey;
    final String FILE_PATH;
    String parentGameKey;
    ArrayList<String> playersWhiteList = new ArrayList<>();

    //game fields
    Clazz clazz;
    Race race;
    Background background;
    Integer experience = 0;
    ArrayList<Integer> abilityScores = new ArrayList<>(); //Strength, Dexterity, Constitution, Intelligence, Wisdom, Charisma
    Integer inspiration = 0;
    ArrayList<Boolean> proficiencies = new ArrayList<>(); //Strength, Dexterity, Constitution, Intelligence, Wisdom, Charisma, Acrobatics, Animal handling, Arcana, Athletics, Deception, History, Insight, Intimidation, Investigation, Medicine, Nature, Perception, Performance, Persuasion, Religion, Sleight of Hand, Stealth, Survival
    ArrayList<Integer> health = new ArrayList<>(); //Max health, current health, temp health
    Integer deathSaves = -1; //>=0 indicates a saving throw must take place. Successes add 1, Fails add 3. %3 to get successes
    ArrayList<Item> items = new ArrayList<>();
    ArrayList<Ability> abilities = new ArrayList<>();
    ArrayList<Integer> spellSlots = new ArrayList<>();
    HashMap<String, Integer> classResources = new HashMap<>();
    HashMap<String, String> descriptors = new HashMap<>();
    transient Integer expertiseMultiplier = 0;
    transient Integer AC = 10;
    transient Integer initiative;
    transient Integer speed;

    //constructors
    public Character(String characterKey, String parentGameKey) {
        this.characterKey = characterKey;
        this.FILE_PATH = "src/main/resources/Games/" + parentGameKey + "/Characters/" + characterKey + ".json";
        this.parentGameKey = parentGameKey;
    }

    public String parseAsString() {
        return characterKey;
    }

    //get methods
    @Override
    public String getKey() {
        return characterKey;
    }

    @Override
    public String getFilePath() {
        return FILE_PATH;
    }

    public String getParentGameKey() {
        return parentGameKey;
    }

    //set methods

    public void setCharacterKey(String characterKey) {
        this.characterKey = characterKey;
    }

    //manage player permissions methods
    public void blacklistPlayer(String playerKey) {
        playersWhiteList.remove(playerKey);
    }

    public void whitelistPlayer(String playerKey) {
        playersWhiteList.add(playerKey);
    }

    public boolean isWhiteListed(String playerKey) {
        return playersWhiteList.contains(playerKey);
    }
}
