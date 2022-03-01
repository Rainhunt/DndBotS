package me.rainh;

import me.rainh.Interfaces.Reference;
import me.rainh.MappedObjects.Character;
import me.rainh.MappedObjects.Game;
import me.rainh.MappedObjects.Player;
import me.rainh.MappedObjects.References.Ability;
import me.rainh.MappedObjects.References.Item;
import me.rainh.MappedObjects.Thread;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.HashMap;
import java.util.NoSuchElementException;
import java.util.Set;

public class ObjectHandler {
    String defaultGame;
    transient HashMap<String, Ability> abilities = new HashMap<>();
    transient HashMap<String, Game> games = new HashMap<>();
    transient HashMap<String, Item> items = new HashMap<>();
    transient HashMap<String, Thread> openThreads = new HashMap<>();
    transient HashMap<String, Player> players = new HashMap<>();

    //load all the default resources on creation
    public ObjectHandler() {
        defaultGame = "default";
        Utils.loadReferencesFromDirectory(Player.class, players, "src/main/resources/Players");
        Utils.loadReferencesFromDirectory(Game.class, games, "src/main/resources/Games");
        for (String game : games.keySet()) {
            String path = "src/main/resources/Games/" + game + "/Characters";
            Utils.loadReferencesFromDirectory(Character.class, games.get(game).getCharactersLoader(), path);
            games.get(game).flushCharactersLoader();
        }
    }

    public String parse() {
        StringBuilder returnMessage = new StringBuilder();
        for (String playerKey : players.keySet()) {
            Player player = players.get(playerKey);
            returnMessage.append("Player:\n").append(">\tKey: ").append(playerKey).append("\n");
            returnMessage.append(">\tFile Path: ").append(player.getFilePath()).append("\n");
            if (player.getActiveCharacters().get(player.getActiveGameKey()) == null) {
                returnMessage.append(">\t\tActive Game: ").append(player.getActiveGameKey()).append("\n");
            }
            for (String activeGame : player.getActiveCharacters().keySet()) {
                if (activeGame.equals(player.getActiveGameKey())) {
                    returnMessage.append(">\t\tActive Game: ").append(activeGame).append(" -> ").append(player.getActiveCharacters().get(activeGame)).append("\n");
                } else {
                    returnMessage.append(">\t\tGame: ").append(activeGame).append(" -> Active Character: ").append(player.getActiveCharacters().get(activeGame)).append("\n");
                }
            }
            returnMessage.append("\n");
        }
        for (String gameKey : games.keySet()) {
            Game game = games.get(gameKey);
            returnMessage.append("Game:\n");
            returnMessage.append(">\tKey: ").append(gameKey).append("\n");
            returnMessage.append(">\tFile Path: ").append(game.getFilePath()).append("\n");
            if (!game.getCharactersMap().isEmpty()) {
                for (String character : game.getCharactersMap().keySet()) {
                    returnMessage.append(">\t\tCharacter: ").append(character).append("\n");
                }
            }
            returnMessage.append("\n");
        }
        for (String threadKey : openThreads.keySet()) {
            System.out.println("Thread key: " + threadKey);
            Thread thread = openThreads.get(threadKey);
            returnMessage.append(">Thread:\n");
            returnMessage.append(">\tThread Key: ").append(threadKey).append("\n");
            for (String whiteListedPlayer : thread.getWhiteListedPlayers()) {
                returnMessage.append(">\t\tWhitelisted Player: ").append(whiteListedPlayer).append("\n");
            }
            returnMessage.append("\n");
        }
        return returnMessage.toString();
    }

    //register object methods
    public void registerReferenceObject(Reference reference) {
        String clazz = reference.getClass().getSimpleName().toLowerCase();
        switch (clazz) {
            case "game" -> games.put(reference.getKey(), (Game) reference);
            case "player" -> players.put(reference.getKey(), (Player) reference);
        }
    } //registers any reference with a direct map in the object handler

    public void registerThread(String threadID, String threadType, String messageID, Reference referenceForEditing) {
        openThreads.put(threadID, new Thread(threadID, threadType, messageID, referenceForEditing));
    }

    public void setDefaultGame(String gameKey) {
        defaultGame = gameKey;
    }

    //get object methods
    public Game getGame(String gameKey) {
        return games.get(gameKey);
    }

    public Player getPlayer(MessageReceivedEvent event) {
        return getPlayer(event.getAuthor().getId());
    }

    public Player getPlayer(String userTag) {
        return players.get(userTag);
    }

    public Thread getThread(String threadID) {
        return openThreads.get(threadID);
    }

    //get key set methods
    public Set<String> getGames() {
        return games.keySet();
    }

    public Set<String> getPlayers() {
        return players.keySet();
    }

    //player access methods
    public Game getActiveGame(String playerKey) throws NoSuchElementException {
        if (games.containsKey(players.get(playerKey).getActiveGameKey())) {
            return games.get(players.get(playerKey).getActiveGameKey());
        } else {
            throw new NoSuchElementException("Player " + playerKey + " has no active game");
        }
    }

    public Game getActiveGame(MessageReceivedEvent event) throws NoSuchElementException {
        return getActiveGame(event.getAuthor().getId());
    }

    public Character getActiveCharacter(String playerKey) throws NoSuchElementException {
        if (getActiveGame(playerKey).getCharactersMap().containsKey(players.get(playerKey).getActiveCharacterKey())) {
            return getActiveGame(playerKey).getCharactersMap().get(players.get(playerKey).getActiveCharacterKey());
        } else {
            throw new NoSuchElementException("Player " + playerKey + " has no active character in their active game " + getActiveGame(playerKey));
        }
    }

    public Character getActiveCharacter(MessageReceivedEvent event) throws NoSuchElementException {
        return getActiveCharacter(event.getAuthor().getId());
    }
}
