package me.rainh.commands;

import me.rainh.Interfaces.Command;
import me.rainh.MappedObjects.Game;
import me.rainh.MappedObjects.Player;
import me.rainh.ObjectHandler;
import me.rainh.Utils;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class Set implements Command {
    @Override
    public void run(MessageReceivedEvent event, ObjectHandler objectHandler) {
        // /set [group] [new value]
        String group = Utils.getSubCommand(event);
        String newValue = Utils.getCommandBody(event);
        String returnMessage = "'Set' command error. Please send a bug report";
        //validate syntax
        if (group == null) {
            returnMessage = "Invalid syntax. Command has no body";
        } else if (newValue == null) {
            returnMessage = "Invalid syntax. No name was provided";
        } else {
            //create and register object
            switch (group) {
                case "activecharacter" -> {
                    Player player = objectHandler.getPlayer(event);
                    Game game = objectHandler.getActiveGame(event);
                    if(!game.getCharactersMap().containsKey(newValue)) {
                        returnMessage = "No character exists with the name " + newValue + " in your currently active game " + game.getKey();
                    } else {
                        player.setActiveCharacter(newValue);
                        //update players json file
                        Utils.writeReferenceToDirectory(player);
                        returnMessage = "Active character set to " + newValue + " in your currently active game " + game.getKey();
                    }
                }
                case "activegame" -> {
                    if (objectHandler.getGame(newValue) == null) {
                        returnMessage = "No game exists with the name " + newValue;
                    } else {
                        Player player = objectHandler.getPlayer(event);
                        player.setActiveGame(newValue);
                        //update players json file
                        Utils.writeReferenceToDirectory(player);
                        returnMessage = "Active game set to " + newValue;
                    }
                }
                default -> returnMessage = "Invalid syntax. The subcommand " + group + "was not found. Use '/help set' for a list of subcommands";
            }
        }
        event.getChannel().sendMessage(returnMessage).queue();
    }
}
