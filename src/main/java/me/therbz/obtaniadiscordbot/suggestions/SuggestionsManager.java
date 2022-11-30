package me.therbz.obtaniadiscordbot.suggestions;

import com.google.gson.Gson;
import me.therbz.obtaniadiscordbot.Main;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;

import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.Normalizer;
import java.util.*;

public class SuggestionsManager {
    private HashMap<Integer, Suggestion> suggestionHashMap;

    public SuggestionsManager() {
        this.suggestionHashMap = new HashMap<>();

        Gson gson = new Gson();

        String json = null;
        try {
            json = new String(Files.readAllBytes(Paths.get("suggestions.json")));
        } catch (IOException e) {
            System.out.println("Could not read from suggestions.json:");
            e.printStackTrace();
        }

        if (!Files.exists(Paths.get("suggestions.json"))) {
            try {
                System.out.println("suggestions.json file non-existent! Creating...");
                Files.createFile(Paths.get("suggestions.json"));
                FileWriter fileWriter = new FileWriter("suggestions.json");
                fileWriter.write(new Gson().toJson(new SuggestionJson()));
                fileWriter.close();
                System.out.println("Successfully created new suggestions.json file!");
            } catch (IOException e) {
                System.out.println("Failed to create suggestions.json:");
                e.printStackTrace();
            }
        }

        SuggestionJson suggestionJson = gson.fromJson(json, SuggestionJson.class);

        if (suggestionJson == null) {
            System.out.println("Suggestions Json File is null!");
            return;
        }

        ArrayList<Suggestion> list = suggestionJson.suggestions;

        if (list != null) {
            list.forEach((Suggestion suggestion) -> {
                this.suggestionHashMap.put(suggestion.getId(), suggestion);
            });
        }

        System.out.println("Successfully loaded " + suggestionHashMap.size() + " suggestions, which is " + ((json == null) ? "null" : json.length()) + " bytes.");
        System.out.println("To prove it, the first suggestion (1) is: \"" + suggestionHashMap.get(1).getContent() + "\"");
    }

    // https://stackoverflow.com/questions/8519669/how-can-non-ascii-characters-be-removed-from-a-string
    private String filterContent(String content) {
        content = Normalizer.normalize(content, Normalizer.Form.NFD);
        return content.replaceAll("[^\\x00-\\x7F]", "");
    }

    public void makeSuggestion(User author, String content, Guild guild) {
        String contentFiltered = this.filterContent(content);

        String authorId = author.getId();
        int nextId = 1;
        while (suggestionHashMap.containsKey(nextId)) {
            nextId++;
        }
        int id = nextId;

        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("Suggestion #" + id + " | !suggest");
        embedBuilder.setColor(new Color(255, 212, 0));
        embedBuilder.setDescription(content); // unfiltered content because it doesn't matter
        embedBuilder.setFooter("Suggested by " + author.getAsTag(), author.getAvatarUrl());
        embedBuilder.setTimestamp(new Date().toInstant());

        guild.getTextChannelById("956228629396860938").sendMessageEmbeds(embedBuilder.build()).queue(botMessage -> {
            botMessage.addReaction("✅").queue();
            botMessage.addReaction("❌").queue();

            // use contentFiltered to save the database
            Suggestion suggestion = new Suggestion(id, botMessage.getId(), authorId, contentFiltered, SuggestionStatus.ONGOING);

            suggestionHashMap.put(id, suggestion);
            this.saveSuggestions();
        });
    }

    public void manuallyAddSuggestion(String messageId, String content) {
        content = this.filterContent(content);

        int nextId = 1;
        while (suggestionHashMap.containsKey(nextId)) {
            nextId++;
        }
        int id = nextId;

        Suggestion newSuggestion = new Suggestion(id, messageId, "-1", content, SuggestionStatus.ONGOING);
        this.suggestionHashMap.put(id, newSuggestion);
        saveSuggestions();
    }

    public Suggestion getSuggestionById(int id) {
        return suggestionHashMap.get(id);
    }

    private void saveSuggestions() {
        SuggestionJson suggestionJson = new SuggestionJson();

        ArrayList<Suggestion> list = new ArrayList<>();
        this.suggestionHashMap.forEach((Integer i, Suggestion s) -> {
            list.add(s);
        });
        suggestionJson.suggestions = list;

        String jsonString = new Gson().toJson(suggestionJson);
        try {
            FileWriter fileWriter = new FileWriter("suggestions.json");
            fileWriter.write(jsonString);
            fileWriter.close();
            System.out.println("Successfully saved " + list.size() + " suggestions and " + jsonString.length() + " bytes to suggestions.json:");
        } catch (IOException e) {
            System.out.println("Failed to write to suggestions.json:");
            e.printStackTrace();
        }
    }

    public void setSuggestionStatus(int id, SuggestionStatus status) {
        this.suggestionHashMap.get(id).setSuggestionStatus(status);;
        this.saveSuggestions();
    }

    public void deleteSuggestion(int id) {
        if (!suggestionHashMap.containsKey(id)) return;
        this.suggestionHashMap.remove(id);
        saveSuggestions();
    }
}
