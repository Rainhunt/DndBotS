package me.rainh.ThreadCommands;

import me.rainh.Interfaces.ThreadCommand;
import me.rainh.MappedObjects.Character;
import me.rainh.MappedObjects.Thread;
import me.rainh.ObjectHandler;
import me.rainh.Utils;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class CharacterEditor implements ThreadCommand {
    @Override
    public void run(MessageReceivedEvent event, Thread thread, ObjectHandler objectHandler) {
        String content = event.getMessage().getContentRaw();
        String command = content.split(" ")[0];
        String newValue = Utils.getCommandBody(event);
        Character character = (Character) thread.getReferenceForEditing();

        if (newValue != null) {
            if (content.split(" ")[1].equals("=")) {
                switch (command) {
                    case "name" -> {
                        objectHandler.getActiveGame(event).removeCharacter(character.getKey());
                        character.setCharacterKey(newValue);
                        objectHandler.registerReferenceObject(character);
                        event.getChannel().editMessageById(thread.getActiveMessageID(), character.parseAsString()).queue();
                    }
                }
            }
        }
    }
}
