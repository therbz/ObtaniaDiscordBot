package me.therbz.obtaniadiscordbot;

import com.google.gson.Gson;
import net.dv8tion.jda.api.entities.User;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class PermissionsHandler {
    private HashSet<String> modIds;

    public PermissionsHandler() {
        modIds = new HashSet<>();

        File modsFile = new File("mods.txt");
        try {
            FileReader fileReader = new FileReader(modsFile);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line = bufferedReader.readLine();
            while (line != null) {
                modIds.add(line);
                line = bufferedReader.readLine();
            }
            bufferedReader.close();
            fileReader.close();
            System.out.println("Successfully read mods.txt. There are " + modIds.size() + " mod accounts.");
        } catch (IOException e) {
            System.out.println("Failed to read mods.txt. As a fallback, no mods have been set. Stacktrace:");
            e.printStackTrace();
        }
    }

    public boolean userIsMod(User user) {
        String userId = user.getId();
        return modIds.contains(userId);
    }
}
