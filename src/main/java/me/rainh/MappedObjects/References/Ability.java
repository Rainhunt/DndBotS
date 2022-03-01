package me.rainh.MappedObjects.References;

import me.rainh.Interfaces.Reference;

public class Ability implements Reference {
    String abilityKey;
    final String FILE_PATH;

    public Ability(String abilityKey) {
        this.abilityKey = abilityKey;
        this.FILE_PATH = "src/main/resources/Abilities/" + abilityKey + ".json";
    }

    @Override
    public String getKey() {
        return abilityKey;
    }

    @Override
    public String getFilePath() {
        return FILE_PATH;
    }
}