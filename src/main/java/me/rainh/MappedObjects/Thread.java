package me.rainh.MappedObjects;

import me.rainh.Interfaces.Reference;

import java.util.ArrayList;

public class Thread {
    String threadID;
    String threadType;
    String activeMessageID;
    Reference referenceForEditing;
    ArrayList<String> whiteListedPlayers = new ArrayList<>(); //ArrayList<playerKey>

    //constructors
    public Thread(String threadID, String threadType, String activeMessageID, Reference referenceEditorObject) {
        this.threadID = threadID;
        this.threadType = threadType;
        this.activeMessageID = activeMessageID;
        this.referenceForEditing = referenceEditorObject;
    }

    //thread manager methods
    public String getThreadType() {
        return threadType;
    }

    //player manager methods
    public void whiteListPlayer(String playerKey) {
        if (!whiteListedPlayers.contains(playerKey)) {
            whiteListedPlayers.add(playerKey);
        }
    }

    public void blackListPlayer(String playerKey) {
        whiteListedPlayers.remove(playerKey);
    }

    public ArrayList<String> getWhiteListedPlayers() {
        return whiteListedPlayers;
    }

    //thread editor methods
    public String getActiveMessageID() {
        return activeMessageID;
    }

    public Reference getReferenceForEditing() {
        return referenceForEditing;
    }
}
