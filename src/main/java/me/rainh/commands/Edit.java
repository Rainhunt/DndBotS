package me.rainh.commands;

import me.rainh.*;
import me.rainh.Interfaces.Command;
import me.rainh.MappedObjects.Character;
import me.rainh.MappedObjects.Game;
import me.rainh.MappedObjects.Player;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class Edit implements Command {
    @Override
    public void run(MessageReceivedEvent event, ObjectHandler objectHandler) {
        // /edit [type] [name]
        String objectType = Utils.getSubCommand(event);
        String objectKey = Utils.getCommandBody(event);
        String returnMessage = "'New' command error. Please send a bug report";
        //validate syntax
        if (objectType == null) {
            returnMessage = "Invalid syntax. Command has no body";
        } else if (objectKey == null) {
            //single word commands
            switch (objectType) {
                case "activecharacter" -> {
                    Player player = objectHandler.getPlayer(event);
                    Character character = objectHandler.getActiveCharacter(player.getKey());
                    if (character == null) {
                        returnMessage = "You have no active character in your currently active game " + player.getActiveGameKey();
                    } else {
                        event.getMessage().createThreadChannel(character.getKey()).queue(threadChannel -> threadChannel.sendMessage(character.parseAsString()).queue(message -> objectHandler.registerThread(threadChannel.getId(), "characterEditor", message.getId(), character)));
                    }
                }
            }
        } else {
            //two word commands
            switch (objectType) {
                case "character" -> {
                    String playerKey = event.getAuthor().getId();
                    Character character = objectHandler.getActiveCharacter(playerKey);
                    Game game = objectHandler.getActiveGame(playerKey);
                    if (!game.getCharactersMap().containsKey(objectKey)) {
                        returnMessage = "No character exists with the name " + objectKey + " in your currently active game " + game.getKey();
                    } else if(!character.isWhiteListed(playerKey)) {
                        returnMessage = "You do not have permission to edit this character";
                    } else {
                        event.getMessage().createThreadChannel(character.getKey()).queue(threadChannel -> threadChannel.sendMessage(character.parseAsString()).queue(message -> objectHandler.registerThread(threadChannel.getId(), "characterEditor", message.getId(), character)));
                    }
                }
            }
        }
        event.getChannel().sendMessage(returnMessage).queue();
    }
}
