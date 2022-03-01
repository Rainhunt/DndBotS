package me.rainh.MappedObjects.References;

import me.rainh.Interfaces.Reference;

public class Race implements Reference {
    String raceKey;
    final String FILE_PATH;

    public Race(String raceKey) {
        this.raceKey = raceKey;
        this.FILE_PATH = "src/main/resources/Races/" + raceKey + ".json";
    }

    @Override
    public String getKey() {
        return raceKey;
    }

    @Override
    public String getFilePath() {
        return FILE_PATH;
    }
}
