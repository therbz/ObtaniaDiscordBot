package me.therbz.obtaniadiscordbot;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;

import javax.security.auth.login.LoginException;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

public class Main{
    public static JDA jda;
    private static DataStorage dataStorage;

    public static void main(String[] args) throws LoginException, IOException {
        JDABuilder jdaBuilder = JDABuilder.createDefault(new String(Files.readAllBytes(Paths.get("token.txt"))));
        jdaBuilder.addEventListeners(new MessageListener());
        jdaBuilder.addEventListeners(new ButtonListener());
        jdaBuilder.addEventListeners(new ReactionListener());
        jdaBuilder.setActivity(Activity.watching("ObtaniaMC.com"));
        jda = jdaBuilder.build();

        dataStorage = new DataStorage();
    }

    public static DataStorage getDataStorage() {
        return dataStorage;
    }
}
