package me.rainh.commands;

import me.rainh.Interfaces.Command;
import me.rainh.MappedObjects.Character;
import me.rainh.MappedObjects.Game;
import me.rainh.MappedObjects.Player;
import me.rainh.ObjectHandler;
import me.rainh.Utils;
import me.rainh.exceptions.ReferenceKeyAlreadyExistsException;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.NoSuchElementException;

public class New implements Command {
    @Override
    public void run(MessageReceivedEvent event, ObjectHandler objectHandler) {
        // /new [type] [name]
        String type = Utils.getSubCommand(event);
        String name = Utils.getCommandBody(event);
        String returnMessage = "'New' command error. Please send a bug report";
        System.out.println(type);
        System.out.println(name);
        //validate syntax
        if (type == null) {
            returnMessage = "Invalid syntax. Command has no body";
        } else if (name == null) {
            returnMessage = "Invalid syntax. No name was provided";
        } else {
            //create and register object
            switch (type) {
                case "game" -> {
                    try {
                        Game game = newGame(name, objectHandler);
                        game.setDM(event.getAuthor().getId());
                        Player player = objectHandler.getPlayer(event);
                        if (player.getActiveGameKey() == null) {
                            player.setActiveGame(name);
                            returnMessage += " and has been set as your active game";
                        }
                    } catch (ReferenceKeyAlreadyExistsException e) {
                        returnMessage = e.getMessage();
                    }
                }
                case "character" -> {
                    String activeGame = objectHandler.getActiveGame(event).getKey();
                    try {
                        Character character = newCharacter(name, activeGame, objectHandler);
                        event.getMessage().createThreadChannel("Character Editor: " + name).queue(threadChannel -> threadChannel.sendMessage(character.parseAsString()).queue(message -> {
                            objectHandler.registerThread(threadChannel.getId(), "characterEditor", message.getId(), character);
                            objectHandler.getThread(threadChannel.getId()).whiteListPlayer(event.getAuthor().getId());
                        }));
                        returnMessage = "New character created in your active game. Your active game is: " + activeGame;
                    } catch (NoSuchElementException e) {
                        returnMessage = "You do not have an active game. Use '/set activegame [name]' to set your active game";
                    } catch (ReferenceKeyAlreadyExistsException e) {
                        returnMessage = "A character already exists with that name in your active game\nYour active game is: " + activeGame;
                    }
                }
                default -> returnMessage = "Invalid syntax. No type" + type + "was found. Use '/help new' for a list of objects that can be created";
            }
        }
        event.getChannel().sendMessage(returnMessage).queue();
    }

    public Character newCharacter(String characterKey, String gameKey, ObjectHandler objectHandler) throws NoSuchElementException, ReferenceKeyAlreadyExistsException {
        if (objectHandler.getGame(gameKey) == null) {
            throw new NoSuchElementException("Game \"" + gameKey + "\" does not exist");
        } else if (objectHandler.getGame(gameKey).getCharactersMap().containsKey(characterKey)) {
            throw new ReferenceKeyAlreadyExistsException("Character \"" + characterKey + "\" already exists");
        } else {
            Character character = new Character(characterKey, gameKey);
            Utils.writeReferenceToDirectory(character);
            objectHandler.getGame(gameKey).addCharacter(character);
            return character;
        }
    }

    public Game newGame(String gameKey, ObjectHandler objectHandler) throws ReferenceKeyAlreadyExistsException {
        if (objectHandler.getGame(gameKey) == null) {
            Game game = new Game(gameKey);
            try {
                String path = "src/main/resources/Games/" + gameKey;
                Files.createDirectories(Paths.get(path));
                Utils.writeReferenceToDirectory(game);
                Files.createDirectories((Paths.get(path + "/Characters")));
                objectHandler.registerReferenceObject(game);
            } catch (Exception e) {
                throw new InstantiationError("Failed to create new game");
            }
            return game;
        } else {
            throw new ReferenceKeyAlreadyExistsException("Game " + gameKey + " already exists");
        }
    }
}
