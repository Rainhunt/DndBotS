package me.rainh.MappedObjects.References;

import me.rainh.Interfaces.Reference;

public class Clazz implements Reference {
    String classKey;
    final String FILE_PATH;

    public Clazz(String classKey) {
        this.classKey = classKey;
        this.FILE_PATH = "src/main/resources/Classes/" + classKey + ".json";
    }

    @Override
    public String getKey() {
        return classKey;
    }

    @Override
    public String getFilePath() {
        return FILE_PATH;
    }
}