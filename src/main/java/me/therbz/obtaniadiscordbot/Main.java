package me.therbz.obtaniadiscordbot;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;

import javax.security.auth.login.LoginException;

public class Main{
    public static JDA jda;
    private static DataStorage dataStorage;

    public static void main(String[] args) throws LoginException {
        JDABuilder jdaBuilder = JDABuilder.createDefault(args[0]);
        jdaBuilder.addEventListeners(new MessageListener());
        jdaBuilder.addEventListeners(new ButtonListener());
        jdaBuilder.addEventListeners(new ReactionListener());
        jdaBuilder.setActivity(Activity.watching("ObtaniaTowny.com"));
        jda = jdaBuilder.build();

        dataStorage = new DataStorage();
    }

    public static DataStorage getDataStorage() {
        return dataStorage;
    }
}
