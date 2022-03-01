package me.rainh.MappedObjects.References;

import me.rainh.Interfaces.Reference;

public class Background implements Reference {
    String backgroundKey;
    final String FILE_PATH;

    public Background(String backgroundKey) {
        this.backgroundKey = backgroundKey;
        this.FILE_PATH = "src/main/resources/Backgrounds/" + backgroundKey + ".json";
    }

    @Override
    public String getKey() {
        return backgroundKey;
    }

    @Override
    public String getFilePath() {
        return FILE_PATH;
    }
}