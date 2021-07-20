package me.therbz.obtaniadiscordbot;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Emote;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.awt.*;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class MessageListener extends ListenerAdapter {
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getAuthor().isBot()) {
            return;
        }

        System.out.println("Message received: " + event.getMessage().getContentRaw());

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
            embedBuilder.setTitle("Towny Suggestion // !suggest");
            embedBuilder.setColor(new Color(255, 212, 0));
            embedBuilder.setDescription(suggestion);
            embedBuilder.setFooter("Suggested by " + event.getMember().getUser().getAsTag());
            embedBuilder.setTimestamp(new Date().toInstant());

            event.getGuild().getTextChannelById("699524366660010055").sendMessage(embedBuilder.build()).queue(botMessage -> {
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
}
