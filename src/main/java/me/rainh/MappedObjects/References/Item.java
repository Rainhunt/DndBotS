package me.rainh.MappedObjects.References;

import me.rainh.Interfaces.Reference;

public class Item implements Reference {
    String itemKey;
    final String FILE_PATH;

    public Item(String itemKey) {
        this.itemKey = itemKey;
        this.FILE_PATH = "src/main/resources/Items/" + itemKey + ".json";
    }

    @Override
    public String getKey() {
        return itemKey;
    }

    @Override
    public String getFilePath() {
        return FILE_PATH;
    }
}