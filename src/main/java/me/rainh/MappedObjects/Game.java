package me.rainh.MappedObjects;

import me.rainh.Interfaces.Reference;

import java.util.ArrayList;
import java.util.HashMap;

public class Game implements Reference {
    //class variables
    String gameKey;
    final String FILE_PATH;
    String DMPlayerKey;
    ArrayList<String> players = new ArrayList<>();
    transient HashMap<String, Character> characters = new HashMap<>();
    transient HashMap<String, Group> groups = new HashMap<>();

    //constructors
    public Game(String key) {
        this.gameKey = key;
        this.FILE_PATH = "src/main/resources/Games/" + key + "/config.json";
    }

    //Reference interface methods
    @Override
    public String getKey() {
        return gameKey;
    }

    @Override
    public String getFilePath() {
        return FILE_PATH;
    }

    //manage character methods
    HashMap<String, Character> charactersLoader = new HashMap<>();
    public HashMap<String, Character> getCharactersLoader() {
        return charactersLoader;
    }

    public void flushCharactersLoader() {
        characters = new HashMap<>();
        for(String character : charactersLoader.keySet()) {
            characters.put(character, charactersLoader.get(character));
        }
        charactersLoader = new HashMap<>();
    }

    public void addCharacter(Character character) {
        characters.put(character.getKey(), character);
    }

    public Character getCharacter(String characterKey) {
        return characters.get(characterKey);
    }

    public void removeCharacter(String characterKey) {
        characters.remove(characterKey);
    }

    public HashMap<String, Character> getCharactersMap() {
        return characters;
    }

    //manage player methods
    public void setDM(String playerKey) {
        DMPlayerKey = playerKey;
    }

    public void addPlayer(String playerKey) {
        players.add(playerKey);
    }

    public void removePlayer(String playerKey) {
        players.remove(playerKey);
    }
}
