package me.rainh.Interfaces;

import me.rainh.ObjectHandler;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public interface Command {
    void run(MessageReceivedEvent event, ObjectHandler objectHandler);
}
