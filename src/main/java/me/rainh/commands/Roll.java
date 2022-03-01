package me.rainh.commands;

import me.rainh.Interfaces.Command;
import me.rainh.ObjectHandler;
import me.rainh.Utils;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.ArrayList;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Roll implements Command {
    private static final Pattern rollCommandSyntax = Pattern.compile("^(\\D*)(\\d+)d(\\d+)(([+,\\-,\\/,\\*]\\d+)*).*$");

    public static ArrayList<Integer> roll(String rollTypeModifier, int numOfDice, int sizeOfDice) throws NoSuchMethodException {
        ArrayList<Integer> output = new ArrayList<>();
        Random random = new Random();

        switch (rollTypeModifier) {
            case "!", "sum", "!sum", "" -> {
                for (int i = 0; i < numOfDice; i++) {
                    int newNumber = random.nextInt(sizeOfDice) + 1; //Random.nextInt() returns a value starting from 0
                    output.add(newNumber);
                    if (newNumber == sizeOfDice && (rollTypeModifier.equals("!") || rollTypeModifier.equals("!sum"))) {
                        i -= 1;
                    }
                }
            }
            case "ability scores", "as" -> {
                double average = 0;
                //return error -1 if lower bound is greater than upper bound
                if (numOfDice > sizeOfDice) {
                    output.set(0, -1);
                    break;
                }
                //return error -2 if upper or lower boundaries are out of bounds
                if (numOfDice > 17 | sizeOfDice < 6) {
                    output.set(0, -2);
                    break;
                }
                //create 6 empty cells for the AS and one for the average
                for (int i = 0; i < 6; i++) {
                    output.add(0);
                }

                //loop until bound requests are met
                while (average == 0) {
                    for (int i = 0; i < 6; i++) {
                        int newAS = Utils.mapASProbability(random.nextInt(10028) + 1);
                        output.set(i, newAS); //set in index 1-6 new AS score
                        average += newAS;
                    }
                    average /= 6;
                    if (average < numOfDice | average > sizeOfDice) average = 0;
                }
            }
            case "average" -> {}
            default -> throw new NoSuchMethodException("No roll type " + rollTypeModifier);
        }
        return output;
    }

    @Override
    public void run(MessageReceivedEvent event, ObjectHandler objectHandler) {
        String content = event.getMessage().getContentRaw();
        String returnMessage;
        if (content.split(" ").length > 1) { //Null safety
            Matcher matcher = rollCommandSyntax.matcher(content.substring(content.indexOf(" ") + 1));
            if (matcher.find()) {
                String rollTypeModifier = matcher.group(1).trim().toLowerCase();
                int numOfDice = Integer.parseInt(matcher.group(2));
                int sizeOfDice = Integer.parseInt(matcher.group(3));
                String mathematicalOperator = matcher.group(5);
                try {
                    ArrayList<Integer> rolls = roll(rollTypeModifier, numOfDice, sizeOfDice);
                    switch (rollTypeModifier) {
                        case "!" -> returnMessage = "Your roll is: " + rolls + "Your roll popped " + (rolls.size() - numOfDice) + " times";
                        case "ability scores", "as" -> {
                            switch (rolls.get(0)) {
                                case -1 -> returnMessage = "Invalid input for '/roll as' -> Lower bound is greater than upper bound";
                                case -2 -> returnMessage = "Invalid input for '/roll as' -> Requested average is out of requested bounds";
                                default -> {
                                    double average = 0;
                                    for (int roll : rolls) {
                                        average += roll;
                                    }
                                    average /= 6;
                                    returnMessage = "Ability scores: " + rolls + "Average: " + average;
                                }
                            }
                        }
                        case "average" -> returnMessage = "The average of your roll is: " + (((double) sizeOfDice + 1) / 2 * (double) numOfDice);
                        case "sum" -> {
                            int sum = 0;
                            for (int roll : rolls) {
                                sum += roll;
                            }
                            returnMessage = "Your roll is: " + rolls + "The sum of your roll is: " + sum;
                        }
                        default -> returnMessage = "Your roll is: " + rolls;
                    }
                } catch (NoSuchMethodException e) {
                    returnMessage = "Roll type " + rollTypeModifier + " not found";
                }
            } else {
                returnMessage = "Invalid syntax. Use -> '/roll [type] #d# [mathematical operator]'";
            }
        } else {
            returnMessage = "Invalid syntax. Roll command has no body";
        }
        event.getChannel().sendMessage(returnMessage).queue();
    }
}
