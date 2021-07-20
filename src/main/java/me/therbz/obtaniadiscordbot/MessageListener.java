package me.therbz.obtaniadiscordbot;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Emote;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.Button;

import java.awt.*;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class MessageListener extends ListenerAdapter {
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getAuthor().isBot()) {
            return;
        }

        System.out.println("[#" + event.getChannel().getName() + "] Message received from " + event.getAuthor().getAsTag() + ": " + event.getMessage().getContentRaw());

        Message message = event.getMessage();
        String[] messageSplit = message.getContentRaw().split(" ");

        /*
        !suggest
         */
        if (messageSplit[0].equalsIgnoreCase("!suggest")) {
            message.delete().queue();

            if (messageSplit.length < 2) {
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
            embedBuilder.setFooter("Suggested by " + event.getAuthor().getAsTag(), message.getAuthor().getAvatarUrl());
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

        /*
         !bugreport
         */
        else if (messageSplit[0].equalsIgnoreCase("!bugreport")) {
            message.delete().queue();

            if (messageSplit.length < 2) {
                event.getChannel().sendMessage("<@" + event.getAuthor().getId() + ">\nYou must supply a message! Example: `!bugreport Using /spawn doesn't work`").queue(botMessage -> {
                    botMessage.delete().queueAfter(10, TimeUnit.SECONDS);
                });
                return;
            }

            String suggestion = message.getContentRaw().replace("!suggest", "");

            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setTitle("Towny Suggestion | !suggest");
            embedBuilder.setColor(new Color(255, 212, 0));
            embedBuilder.setDescription(suggestion);
            embedBuilder.setFooter("Suggested by " + event.getAuthor().getAsTag(), message.getAuthor().getAvatarUrl());
            embedBuilder.setTimestamp(new Date().toInstant());

            event.getGuild().getTextChannelById("699524366660010055").sendMessageEmbeds(embedBuilder.build())/*.setActionRow(Button.success("upvote", "Upvote"), Button.danger("downvote", "Downvote"))*/.queue(botMessage -> {
                botMessage.addReaction("✅").queue();
                botMessage.addReaction("❌").queue();
            });
        }

        else if (messageSplit[0].equalsIgnoreCase("!createbugreportmessage")) {
            if (event.getAuthor().getId().equals("222790155952586752")) {
                EmbedBuilder embedBuilder = new EmbedBuilder();
                embedBuilder.setTitle("Open a Bug Report");
                embedBuilder.setColor(new Color(255, 212, 0));
                embedBuilder.setDescription("Open a Bug Report by clicking the button below.\nYou will be taken to a separate private channel to explain the bug and provide any helpful information.");

                event.getChannel().sendMessageEmbeds(embedBuilder.build()).setActionRow(Button.primary("open_bug_report", "Open a Bug Report")).queue();
            }
        }
    }

    // https://github.com/DV8FromTheWorld/JDA/wiki/Interactions#buttons
    @Override
    public void onButtonClick(ButtonClickEvent event) {
        switch (event.getComponentId()) {
            case "upvote":
                event.deferEdit().queue();
                break;
            case "downvote":
                event.deferEdit().queue();
                break;
            case "open_bug_report":
                event.deferEdit().queue();

                String channelName = "bug-report-" + generateBugReportId(6);

                event.getGuild().createTextChannel(channelName, event.getGuild().getCategoryById("867104424638677013")).queue(channel -> {
                    channel.createPermissionOverride(event.getMember()).setAllow(Permission.MESSAGE_READ).queue();

                    EmbedBuilder embedBuilder = new EmbedBuilder();
                    embedBuilder.setAuthor("Bug Report opened by " + event.getUser().getAsTag(), null, event.getUser().getAvatarUrl());
                    embedBuilder.setColor(new Color(255, 212, 0));
                    embedBuilder.setDescription("Please use the Bug Report format as follows:\n\n**1) Description of the bug**\n**2) Steps to reproduce the bug**\n**3) What you think the actual result should be**\n\nPlease provide screenshots if applicable.");
                    embedBuilder.setFooter("Click \"Close Bug Report\" to close this bug report.");

                    channel.sendMessageEmbeds(embedBuilder.build()).setActionRow(Button.danger("close_bug_report", "Close Bug Report"), Button.primary("bug_report_mention_therbz", "@ therbz")).queue();

                    event.getChannel().sendMessage("<@" + event.getUser().getId() + "> Created your Bug Report at <#" + channel.getId() + ">").queue(botMessage -> {
                        botMessage.delete().queueAfter(10, TimeUnit.SECONDS);
                    });
                });

                System.out.println("Opened a Bug Report by " + event.getUser().getAsTag() + " in channel " + channelName);

                break;
            case "close_bug_report":
                event.deferEdit().queue();
                event.getTextChannel().delete().queue();
                System.out.println("Closed Bug Report " + event.getTextChannel().getName());
                break;
            case "bug_report_mention_therbz":
                event.deferEdit().queue();
                event.getTextChannel().sendMessage("<@222790155952586752> Bug report assistance requested by " + event.getUser().getName()).queue();
                break;
        }
    }

    private String generateBugReportId(int stringLength) {
        int leftLimit = 48; // letter '0'
        int rightLimit = 57; // Letter '9'
        // int rightLimit = 122; // letter 'z'
        Random random = new Random();

        return random.ints(leftLimit, rightLimit + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(stringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }
}
