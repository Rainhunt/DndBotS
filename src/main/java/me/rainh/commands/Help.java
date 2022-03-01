package me.rainh.commands;

import me.rainh.Interfaces.Command;
import me.rainh.ObjectHandler;
import me.rainh.Utils;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class Help implements Command {
    @Override
    public void run(MessageReceivedEvent event, ObjectHandler objectHandler) {
        // /help [command] [subcommand] subgroup is optional
        String group = Utils.getSubCommand(event);
        String subGroup = Utils.getCommandBody(event);
        String returnMessage = "'List' command error. Please send a bug report";
        //validate syntax
        if (group == null) {
            returnMessage = "```md\n";
            returnMessage += "List of commands:\n";
            returnMessage += "* /Edit <type> <name> -> opens an editor panel for a <type> object named <name>\n";
            returnMessage += "* /List <type> -> returns a list of all <type> objects\n";
            returnMessage += "* /New <type> <name> -> creates a new <type> object named <name>\n";
            returnMessage += "* /Roll [type] <number_of_dice>d<size_of_die> [mathematical_operator] -> generates a set of <number_of_dice> random numbers between 1 and <size_of_die>";
            returnMessage += "* /Set <field> <new_value> -> sets <field> to <new value>\n";
            returnMessage += "```";
        } else if (subGroup == null) {
            returnMessage = "```md\n";
            switch (group) {
                case "charactereditor" -> {
                        returnMessage += "name = <name> -> changes the name of your character\n";
                }
                case "edit" -> {
                    returnMessage += "* /edit character <name> -> opens a new character editor panel.\nUse '/help charactereditor' for a list of commands the character editor accepts\n";
                }
                case "list" -> {
                    returnMessage += "* /list characters -> returns a list of all characters in your active game\n";
                    returnMessage += "* /list games -> returns a list of all games in the server\n";
                    returnMessage += "* /list players -> returns a list of all players in the server\n";
                }
                case "new" -> {
                    returnMessage += "* /new character <name> -> creates a new character in your active game\n";
                    returnMessage += "* /new game <name> -> creates a new game\n";
                }
                case "roll" -> {
                    returnMessage += "* /roll <number_of_dice>d<size_of_die> -> returns a set of <number_of_dice> random numbers between 1 and <size_of_die>\n";
                    returnMessage += "* /roll ! <number_of_dice>d<size_of_die> -> returns the requested roll. Every time the number generated is the maximum number, an additional roll is rolled";
                    returnMessage += "* /roll as <lower_bound>d<upper_bound> -> returns a set of 6 numbers using 4d6 drop lowest rolling that have a mean average between (and including) the lower and upper bounds\n";
                    returnMessage += "* /roll average <number_of_dice>d<size_of_die> -> returns the expected average of the specified roll\n";
                    returnMessage += "* /roll sum <number_of_dice>d<size_of_die> [mathematical_operator] -> returns the sum of the requested roll, alongside the array of numbers. If mathematical operators are used, the sum is returned after evaluating the whole expression\n";
                }
                case "set" -> {
                    returnMessage += "* /set activegame <game> -> sets your active game to <game>\n";
                    returnMessage += "* /set activecharacter <character> -> sets your active character in your active game to <character>\n";
                }
            }
            returnMessage += "```";
        }
        event.getChannel().sendMessage(returnMessage).queue();
    }
}
