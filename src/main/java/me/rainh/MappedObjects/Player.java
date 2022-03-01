package me.rainh.MappedObjects;

import me.rainh.Interfaces.Reference;

import java.util.HashMap;

public class Player implements Reference {
    //class variables
    String userTag;
    final String FILE_PATH;
    String activeGameKey;
    HashMap<String, String> activeCharacters = new HashMap<>(); //HashMap<gameKey, characterKey>

    //constructors
    public Player(String userTag, String activeGameKey) {
        this.userTag = userTag;
        this.FILE_PATH = "src/main/resources/Players/" + userTag + ".json";
        this.activeGameKey = activeGameKey;
    }

    //Reference interface methods
    @Override
    public String getKey() {
        return userTag;
    }

    @Override
    public String getFilePath() {
        return FILE_PATH;
    }

    //get methods
    public String getActiveCharacterKey() {
        return activeCharacters.get(activeGameKey);
    }

    public HashMap<String, String> getActiveCharacters() {
        return activeCharacters;
    }

    public String getActiveGameKey() {
        return activeGameKey;
    }

    //set methods
    public void setActiveCharacter(String characterKey) {
        activeCharacters.put(activeGameKey, characterKey);
    }

    public void setActiveGame(String gameKey) {
        this.activeGameKey = gameKey;
    }
}
