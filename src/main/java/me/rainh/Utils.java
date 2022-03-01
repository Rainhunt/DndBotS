package me.rainh;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import me.rainh.Interfaces.Reference;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

public class Utils {

    //create and run a thread pool async
    private static final ExecutorService exe = Executors.newCachedThreadPool();

    public static void asyncTask(Runnable task) {
        exe.submit(task);
    }

    //return the second word of a MessageReceivedEvent. Returns null if there is only one word
    public static String getSubCommand(MessageReceivedEvent event) {
        String content = event.getMessage().getContentRaw();
        if (content.split(" ").length < 2) {
            return null;
        } else {
            return content.split(" ")[1];
        }
    }

    //return the string starting from the third word. Returns null if there are less than three words
    public static String getCommandBody(MessageReceivedEvent event) {
        String content = event.getMessage().getContentRaw();
        if (content.split(" ").length < 3) {
            return null;
        } else {
            return content.substring(content.indexOf(" ", content.indexOf(" ") + 1) + 1);
        }
    }

    //create an object of type <T> for every file in a directory, and then put them in map with the file name as a key
    public static <T> void loadReferencesFromDirectory(Class<T> type, HashMap<String, T> map, String filePath) {
        File[] files = new File(filePath).listFiles();

        //null safety
        if (files == null) {
            System.out.println(filePath + " is empty");
            return;
        }

        Gson gson = new Gson();
        //deserialize each json file and put the created object in its map
        for (File file : files) {
            String fileName = file.getName().substring(0, file.getName().length() - 5); //remove the .json suffix
            String path = file.getPath().replace("\\", "/");
            if (type.getSimpleName().equals("Game")) {
                fileName = file.getName();
                path += "/config.json";
            }
            file = new File(path);

            //make sure the Json file is parsed correctly
            try (Reader reader = new FileReader(file)) {
                T object = gson.fromJson(reader, type);
                map.put(fileName, object);

                //error output
            } catch (JsonParseException | FileNotFoundException e) {
                System.out.println(file.getName() + " is incorrectly parsed");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //write a reference instance to its JSON resource
    public static void writeReferenceToDirectory(Reference reference) {
        System.out.println("Writing: " + reference.getKey() + " to file");
        String jsonString = new GsonBuilder().setPrettyPrinting().create().toJson(reference);
        String path = reference.getFilePath();
        System.out.println(path);

        File jsonFile = new File(path);
        try (PrintWriter writer = new PrintWriter(jsonFile)) {
            writer.print(jsonString);
            System.out.println("Content written to file");
            System.out.println("End");
        } catch (Exception e) {
            System.out.println("Failed to write text to file");
            System.out.println(jsonString);
            System.out.println(path);
            System.out.println("End");
        }
    }

    //match a random number between 1 and 10028 to the appropriate chance give 4d6 drop lost
    public static int mapASProbability(int number) {
        if (number < 8) return 3;
        else if (number < 39) return 4;
        else if (number < 116) return 5;
        else if (number < 278) return 6;
        else if (number < 571) return 7;
        else if (number < 1049) return 8;
        else if (number < 1751) return 9;
        else if (number < 2692) return 10;
        else if (number < 3834) return 11;
        else if (number < 5152) return 12;
        else if (number < 6479) return 13;
        else if (number < 7714) return 14;
        else if (number < 8725) return 15;
        else if (number < 9450) return 16;
        else if (number < 9866) return 17;
        else if (number < 10028) return 18;
        else return 0;
    }

    public static void copyOver(String... names) throws IOException {
        for (String name : names) {
            if (!new File(name).exists()) {
                Files.copy(Thread.currentThread().getContextClassLoader().getResourceAsStream(name), Path.of(name), StandardCopyOption.REPLACE_EXISTING);
           }
        }
    }
}

