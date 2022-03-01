package me.rainh;

import me.rainh.Interfaces.Command;
import me.rainh.Interfaces.ThreadCommand;
import me.rainh.MappedObjects.Player;
import me.rainh.MappedObjects.Thread;
import me.rainh.ThreadCommands.CharacterEditor;
import me.rainh.commands.*;
import net.dv8tion.jda.api.events.channel.update.ChannelUpdateArchivedEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.thread.member.ThreadMemberJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.util.HashMap;

public class EventListener extends ListenerAdapter {
    ObjectHandler objectHandler = new ObjectHandler();
    HashMap<String, Command> commands = new HashMap<>();
    HashMap<String, ThreadCommand> threadCommands = new HashMap<>();
    String displayThread;
    String displayMessage;

    public EventListener() {
        //list of commands
        commands.put("edit", new Edit());
        commands.put("help", new Help());
        commands.put("list", new List());
        commands.put("new", new New());
        commands.put("roll", new Roll());
        commands.put("set", new Set());

        //list of threadCommands
        threadCommands.put("characterEditor", new CharacterEditor());
    }

    @Override
    public void onMessageReceived(@Nonnull MessageReceivedEvent event) {
        //cancel if the message is from a bot
        if (event.getAuthor().isBot()) {
            return;
        }

        //identify and run thread commands
        if (event.isFromThread()) {
            String threadID = event.getThreadChannel().getId();
            if (objectHandler.openThreads.containsKey(threadID)) {
                Thread thread = objectHandler.openThreads.get(threadID);
                threadCommands.get(thread.getThreadType()).run(event, thread, objectHandler);
                event.getMessage().delete().queue();
                return;
            }
        }

        //trim white space and ignore case
        String content = event.getMessage().getContentRaw().trim().toLowerCase();

        //cancel if no command was requested
        if (!content.startsWith("/") || content.length() < 2) {
            return;
        }

        //add a player object if the user has no player
        if (!objectHandler.players.containsKey(event.getAuthor().getId())) {
            addPlayer(event.getAuthor().getId());
        }

        //run a command. If the command isn't found, return an error message
        String command = content.split(" ")[0].substring(1); //get first word
        if (commands.containsKey(command)) {
            Utils.asyncTask(() -> commands.get(command).run(event, objectHandler));
        } else { //send error message if command does not exist
            event.getChannel().sendMessage("Command not found. Try '/help' for a list of commands").queue();
        }

        if (displayThread == null) {
            event.getMessage().createThreadChannel("Display Object Handler").queue(threadChannel -> {
                displayThread = threadChannel.getId();
                threadChannel.sendMessage(objectHandler.parse()).queue(message -> displayMessage = message.getId());
            });
        } else {
            event.getGuild().getThreadChannelById(displayThread).editMessageById(displayMessage, objectHandler.parse()).queue();
        }
    }

    @Override
    public void onThreadMemberJoin(@NotNull ThreadMemberJoinEvent event) {
        if (!objectHandler.players.containsKey(event.getMember().getId())) {
            addPlayer(event.getMember().getId());
        }

        if (!event.getMember().getUser().isBot() && objectHandler.openThreads.containsKey(event.getThread().getId()) && !objectHandler.getThread(event.getThread().getId()).getWhiteListedPlayers().contains(event.getMember().getId())) {
            event.getMember().kick("You do not have permission to access this thread").queue();
        }
    }

    @Override
    public void onChannelUpdateArchived(@NotNull ChannelUpdateArchivedEvent event) {
        String channelId = event.getChannel().getId();
        objectHandler.openThreads.remove(channelId);
    }

    public void addPlayer(String userTag) {
        Player newPlayer = new Player(userTag, objectHandler.defaultGame);
        objectHandler.players.put(userTag, newPlayer);
        Utils.writeReferenceToDirectory(newPlayer);
    }
}

