package me.rainh.MappedObjects;

import me.rainh.Interfaces.Reference;

public class Group implements Reference {
    String groupKey;
    @Override
    public String getKey() {
        return groupKey;
    }

    @Override
    public String getFilePath() {
        return null;
    }
}
