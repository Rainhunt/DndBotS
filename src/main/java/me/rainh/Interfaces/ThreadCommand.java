package me.rainh.Interfaces;

import me.rainh.MappedObjects.Thread;
import me.rainh.ObjectHandler;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public interface ThreadCommand {
    void run(MessageReceivedEvent event, Thread thread, ObjectHandler objectHandler);
}
