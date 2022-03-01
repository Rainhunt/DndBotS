package me.rainh.commands;

import me.rainh.Interfaces.Command;
import me.rainh.ObjectHandler;
import me.rainh.Utils;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class List implements Command {
    @Override
    public void run(MessageReceivedEvent event, ObjectHandler objectHandler) {
        // /list [group] [subgroup] subgroup is optional
        String group = Utils.getSubCommand(event);
        String subGroup = Utils.getCommandBody(event);
        String returnMessage = "'List' command error. Please send a bug report";
        //validate syntax
        if (group == null) {
            returnMessage = "Invalid syntax. Command has no body";
        } else if (subGroup == null) {
            switch (group) {
                case "characters" -> {
                    returnMessage = objectHandler.getActiveGame(event).getCharactersMap().keySet().toString();
                    if (returnMessage == null) {
                        returnMessage = "Your active game does not exist";
                    }
                }
                case "games" -> returnMessage = objectHandler.getGames().toString();
                case "players" -> returnMessage = objectHandler.getPlayers().toString();
                default -> returnMessage = "Invalid syntax. No group " + group + " was found. Use '/help list' for a list of groups that can be listed";
            }
        } else {
            //add subgroup searching later
        }
        event.getChannel().sendMessage(returnMessage).queue();
    }
}

