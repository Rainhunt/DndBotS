package me.rainh;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;

import java.io.FileInputStream;
import java.util.Properties;

public class DndBotS {

    public static void main(String[] args) throws Exception {
        Utils.copyOver("config.properties");
        Properties prop = new Properties();
        prop.load(new FileInputStream("config.properties"));
        JDA api = JDABuilder.createDefault(prop.getProperty("TOKEN")).build();
        api.addEventListener(new EventListener());
    }
}
