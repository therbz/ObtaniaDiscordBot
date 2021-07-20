package me.therbz.obtaniadiscordbot;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Emote;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.Button;

import java.awt.*;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class MessageListener extends ListenerAdapter {
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getAuthor().isBot()) {
            return;
        }

        System.out.println("Message received from '" + event.getAuthor().getAsTag() + "': " + event.getMessage().getContentRaw());

        Message message = event.getMessage();
        if (message.getContentRaw().split(" ")[0].equalsIgnoreCase("!suggest")) {
            message.delete().queue();

            if (message.getContentRaw().split(" ").length < 2) {
                event.getChannel().sendMessage("<@" + event.getAuthor().getId() + ">\nYou must supply a suggestion! Example: `!suggest Add more rats!`").queue(botMessage -> {
                    botMessage.delete().queueAfter(10, TimeUnit.SECONDS);
                });
                return;
            }

            String suggestion = message.getContentRaw().replace("!suggest", "");

            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setTitle("Towny Suggestion | !suggest");
            embedBuilder.setColor(new Color(255, 212, 0));
            embedBuilder.setDescription(suggestion);
            embedBuilder.setFooter("Suggested by " + event.getMember().getUser().getAsTag(), message.getAuthor().getAvatarUrl());
            embedBuilder.setTimestamp(new Date().toInstant());

            event.getGuild().getTextChannelById("699524366660010055").sendMessageEmbeds(embedBuilder.build())/*.setActionRow(Button.success("upvote", "Upvote"), Button.danger("downvote", "Downvote"))*/.queue(botMessage -> {
                botMessage.addReaction("✅").queue();
                botMessage.addReaction("❌").queue();
            });
        }
        else if (event.getChannel() == event.getGuild().getTextChannelById("699524366660010055")) {
            event.getMessage().delete().queue();
            event.getChannel().sendMessage("<@" + event.getAuthor().getId() + ">\nPlease keep suggestions discussion to <#723448278666182737>.\nIf you want to make a suggestion, use `!suggest` in this channel or <#699358412210962496>.").queue(botMessage -> {
                botMessage.delete().queueAfter(10, TimeUnit.SECONDS);
            });
        }
    }

    // https://github.com/DV8FromTheWorld/JDA/wiki/Interactions#buttons
    /*@Override
    public void onButtonClick(ButtonClickEvent event) {
        if (event.getComponentId().equals("upvote")) {
            event.deferEdit().queue();
        } else if (event.getComponentId().equals("downvote")) {
            event.deferEdit().queue();
        }
    }*/
}
