package me.therbz.obtaniadiscordbot;

import net.dv8tion.jda.api.JDABuilder;

import javax.security.auth.login.LoginException;

public class Main{
    public static void main(String[] args) throws LoginException {
        JDABuilder jdaBuilder = JDABuilder.createDefault(args[0]);
        jdaBuilder.addEventListeners(new MessageListener());
        jdaBuilder.build();
    }
}
